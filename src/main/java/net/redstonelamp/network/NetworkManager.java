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
package net.redstonelamp.network;

import net.redstonelamp.Server;
import net.redstonelamp.network.netInterface.AdvancedNetworkInterface;
import net.redstonelamp.ticker.CallableTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Handler class for all Network protocols.
 *
 * @author RedstoneLamp Team
 */
public class NetworkManager{
    private final Server server;
    private final ExecutorService actionPool;
    private final List<Protocol> protocols = new ArrayList<>();

    /**
     * Create a new NetworkManager belonging to the specified <code>Server</code>
     *
     * @param server The <code>Server</code> this NetworkManager belongs to.
     */
    public NetworkManager(Server server){
        this.server = server;
        actionPool = Executors.newFixedThreadPool(4, new PoolThreadFactory());
        server.getTicker().addRepeatingTask(new CallableTask("tick", this), 1);
    }

    public void tick(long tick){
        protocols.forEach(Protocol::tick);
    }

    /**
     * Register a <code>Protocol</code> to this NetworkManager
     *
     * @param protocol The <code>Protocol</code> to be registered
     */
    public synchronized void registerProtocol(Protocol protocol){
        synchronized(protocols){
            protocols.add(protocol);
        }
        server.getLogger().info("Registered protocol: " + protocol.getName());
    }

    /**
     * Sets the name of all AdvancedNetworkInterfaces registered
     *
     * @param name The name to be set to
     */
    public void setName(String name){
        actionPool.execute(() -> protocols.stream().filter(protocol -> protocol._interface instanceof AdvancedNetworkInterface).forEach(protocol -> ((AdvancedNetworkInterface) protocol._interface).setName(name)));
    }

    /**
     * Get a <code>Protocol</code> by it's Class
     *
     * @param clazz The Class of the <code>Protocol</code>
     * @return The <code>Protocol</code> if registered, null if not.
     */
    public synchronized Protocol getProtocol(Class<? extends Protocol> clazz){
        synchronized(protocols){
            for(Protocol protocol : protocols){
                if(protocol.getClass().getName().equals(clazz.getName())){
                    return protocol;
                }
            }
        }
        return null;
    }

    /**
     * Get the <code>Server</code> the NetworkManager belongs to.
     *
     * @return The <code>Server</code> this NetworkManager belongs to.
     */
    public Server getServer(){
        return server;
    }

    public ExecutorService getActionPool() {
        return actionPool;
    }

    public void shutdown() {
        protocols.forEach(Protocol::onShutdown);
        actionPool.shutdown();
    }

    private static class PoolThreadFactory implements ThreadFactory {
        private int currentThread = 0;

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("NetworkProcessor-"+currentThread++);
            return t;
        }
    }
}
