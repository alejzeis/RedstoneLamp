/*
 * This file is part of RedstoneLamp.
 *
 * RedstoneLamp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedstoneLamp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RedstoneLamp.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.redstonelamp.ticker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.redstonelamp.Server;
import net.redstonelamp.cmd.exception.CommandException;
import net.redstonelamp.utils.AntiSpam;

/**
 * The ticker used by RedstoneLamp.
 * <br>
 * It is based on the one used in BlockServer (https://github.com/BlockServerProject/BlockServer)
 *
 * @author RedstoneLamp Team
 */
public class RedstoneTicker{
    private static final String ANTISPAM_LOAD_MEASURE_TOO_HIGH = "net.redstonelamp.ticker.RedstoneTicker.LoadMeasureTooHigh";
    private final Server server;
    private long sleep;
    private long tick = -1L;
    private boolean running = false;
    private boolean lastTickDone = false;
    private long lastTickMilli;
    private double loadMeasure = 0D;
    private long startTime;
    private final List<RegisteredTask> tasks = new ArrayList<>();
    private BufferedReader cli;

    /**
     * Create a new <code>RedstoneTicker</code> belonging to the specified <code>Server</code>
     *
     * @param server     The Server this ticker belongs to.
     * @param sleepNanos The amount of nanoseconds to sleep for. (default 50)
     */
    public RedstoneTicker(Server server, int sleepNanos){
        this.server = server;
        sleep = sleepNanos;
        cli = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Start this ticker. This method actually runs the ticker too, therefor it blocks.
     */
    public void start(){
        if(running){
            throw new IllegalStateException("Ticker is already running");
        }
        running = true;
        server.getLogger().debug("Ticker is now running.");
        startTime = System.currentTimeMillis();
        while(running){
            lastTickMilli = System.currentTimeMillis();
            tick++;
            tick();

            // calculate server load
            long now = System.currentTimeMillis();
            long diff = now - lastTickMilli;
            loadMeasure = diff * 100D / sleep;
            if(loadMeasure > 80D){
                AntiSpam.act(() -> server.getLogger().warning("The server load is too high! (%f / 100)", loadMeasure), ANTISPAM_LOAD_MEASURE_TOO_HIGH, 5000);
                //server.getLogger().warning("The server load is too high! (%f / 100)", loadMeasure);
                continue;
            }
            long need = sleep - diff;
            try{
                Thread.sleep(need);
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            /*
            long diff = System.currentTimeMillis() - lastTickMilli;
            if(diff < sleep) {
                long need = sleep - diff;
                try {
                    Thread.sleep(need);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                AntiSpam.act(() -> server.getLogger().warning("The server load is too high! ("+diff+">"+sleep+")"), ANTISPAM_LOAD_MEASURE_TOO_HIGH, 5000);
            }
            */

        }
        synchronized(tasks){
            for(RegisteredTask task : tasks){
                task.getTask().onFinalize();
            }
        }
        
        
        
        lastTickDone = true;
    }

    private void tick(){
        RegisteredTask[] taskArray;
        synchronized(tasks){
            taskArray = new RegisteredTask[tasks.size()];
            tasks.toArray(taskArray);
        }
        for(RegisteredTask task : taskArray){
            long start = System.currentTimeMillis();
            task.check(tick);
            long elapsed = System.currentTimeMillis() - start;
            if(elapsed >= 20) server.getLogger().debug("Task took "+elapsed+"ms, "+task.getTask());
        }
        
        String line = null;
        try {
            if (cli.ready()) {
                line = cli.readLine();
                if (line != null)
                    server.getCommandManager().executeCommand(line, server);
            }
        } catch (CommandException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {}
    }

    /**
     * Stop this ticker. This method will block until the last tick is done.
     */
    public void stop(){
        if(!running){
            throw new IllegalStateException("Ticker is not running and cannot be stopped");
        }
        running = false;
    }

    public synchronized void addDelayedTask(Task task, int delay){
        synchronized(tasks){
            tasks.add(RegisteredTask.delay(task, delay));
        }
    }

    public synchronized void addRepeatingTask(Task task, int repeatInterval){
        synchronized(tasks){
            tasks.add(RegisteredTask.repeat(task, repeatInterval));
        }
    }

    public synchronized void addDelayedRepeatingTask(Task task, int delay, int repeatInterval){
        synchronized(tasks){
            tasks.add(RegisteredTask.delayAndRepeat(task, delay, repeatInterval));
        }
    }

    /**
     * Cancel a task.
     *
     * @param task The task to be canceled.
     * @return If the task was removed.
     */
    public synchronized boolean cancelTask(Task task){
        RegisteredTask corr = null;
        synchronized(tasks){
            for(RegisteredTask rt : tasks){
                if(rt.getTask().equals(task)){
                    corr = rt;
                    break;
                }
            }
            return tasks.remove(corr);
        }
    }

    public long getStartTime(){
        return startTime;
    }
}
