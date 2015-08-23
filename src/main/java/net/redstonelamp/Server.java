/**
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
package net.redstonelamp;

import net.redstonelamp.config.ServerConfig;
import net.redstonelamp.network.NetworkManager;
import net.redstonelamp.network.Protocol;
import net.redstonelamp.request.LoginRequest;
import net.redstonelamp.ticker.RedstoneTicker;
import net.redstonelamp.ui.Logger;

import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * The base RedstoneLamp server, which handles the ticker.
 *
 * @author RedstoneLamp Team
 */
public class Server implements Runnable{
    private final List<Runnable> shutdownTasks = new ArrayList<>(); //List of tasks to be run on shutdown
    private final Logger logger;
    private final ServerConfig config;
    private final RedstoneTicker ticker;
    private final NetworkManager network;
    private final List<Player> players = new CopyOnWriteArrayList<>();

    /**
     * Package-private constructor used by the RedstoneLamp run class
     * @param logger The server's logger
     * @param config The server's configuration
     */
    Server(Logger logger, ServerConfig config) {
        ticker = new RedstoneTicker(this, 50);
        this.logger = logger;
        this.config = config;
        this.network = new NetworkManager(this);
        logger.info(RedstoneLamp.getSoftwareVersionString() +" is licensed under the Lesser GNU General Public License version 3");
    }

    @Override
    public void run() {
        logger.info(RedstoneLamp.SOFTWARE+" is now running.");
        ticker.start();
    }

    /**
     * Add a <code>Runnable</code> to be ran when the server begins to shutdown. This method is thread-safe.
     * @param r The <code>Runnable</code> to be ran when the server stops.
     */
    public synchronized void addShutdownTask(Runnable r) {
        synchronized (shutdownTasks) {
            shutdownTasks.add(r);
        }
    }

    /**
     * INTERNAL METHOD
     * @param address
     * @param protocol
     * @param loginRequest
     * @return
     */
    public Player openSession(SocketAddress address, Protocol protocol, LoginRequest loginRequest) {
        logger.debug("Opened Session: "+address.toString());
        Player player = new Player(protocol, address.toString());
        players.add(player);
        return player;
    }

    /**
     * INTERNAL METHOD!
     * @param player
     */
    public void closeSession(Player player) {
        logger.debug("Closed Session: "+player.getIdentifier());
        players.remove(player);
    }

    //All Setter/Getter methods BELOW here.

    /**
     * Retrieve the server's logger.
     * @return The <code>Logger</code> instance the server uses.
     */
    public Logger getLogger() {
        return logger;
    }

    public RedstoneTicker getTicker() {
        return ticker;
    }
}
