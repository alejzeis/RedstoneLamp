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

/**
 * Created by jython234 on 8/20/2015.
 *
 * @author RedstoneLamp Team
 */
public class RegisteredTask{
    private Task task;
    private long leftTicks;
    private int repeatInterval;

    public static RegisteredTask delay(Task task, int delay){
        return new RegisteredTask(task, delay, 0);
    }

    public static RegisteredTask repeat(Task task, int repeat){
        return new RegisteredTask(task, 1, repeat);
    }

    public static RegisteredTask delayAndRepeat(Task task, int delay, int repeat){
        return new RegisteredTask(task, delay, repeat);
    }

    public RegisteredTask(Task task, int delay, int repeat){
        this.task = task;
        leftTicks = delay;
        repeatInterval = repeat;
    }

    /**
     * <font color="FF0000"><b>Warning: this method is frequently called.
     * Minimize the time to run this method.</b></font>
     * <br>Number of times run per second: number of tasks * TPS
     *
     * @param tick the current ticker tick
     */
    public void check(long tick){
        leftTicks--;
        if(leftTicks == 0){
            task.onRun(tick);
            leftTicks += repeatInterval;
            // if repeat interval is 0, it will become -1 next time and won't get called again
        }
    }

    /**
     * <b>Warning: this method does not unregister the task.</b> It is only for
     * temporary usage.<br>
     * To unregister totally, use {@link RedstoneTicker#cancelTask(Task)}.
     */
    public void stopRepeating(){
        leftTicks = -1;
    }

    public Task getTask(){
        return task;
    }
}
