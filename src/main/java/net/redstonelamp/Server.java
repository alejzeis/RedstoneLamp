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
import net.redstonelamp.level.LevelManager;
import net.redstonelamp.level.position.Position;
import net.redstonelamp.network.NetworkManager;
import net.redstonelamp.network.Protocol;
import net.redstonelamp.network.pe.PEProtocol;
import net.redstonelamp.request.LoginRequest;
import net.redstonelamp.response.Response;
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

    private String motd;
    private int maxPlayers;
    private LevelManager levelManager;
    private int nextEntityID = 1;

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
        this.levelManager = new LevelManager(this);
        levelManager.init();
        loadProperties(config);
        logger.info(RedstoneLamp.getSoftwareVersionString() +" is licensed under the Lesser GNU General Public License version 3");

        network.registerProtocol(new PEProtocol(network));
        network.setName(motd);
    }

    private void loadProperties(ServerConfig config) {
        maxPlayers = config.getInt("max-players");
        motd = config.getString("motd");
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
        Player player = new Player(protocol, address);
        players.add(player);
        network.setName(motd); //Update the amount of players online
        return player;
    }

    /**
     * INTERNAL METHOD!
     * @param player
     */
    public void closeSession(Player player) {
        logger.debug("Closed Session: "+player.getAddress().toString());
        players.remove(player);
        network.setName(motd); //Update the amount of players online
    }

    /**
     * Broadcasts a response to ALL players on the server. Please use API methods if
     * available.
     * @param r The Request to be broadcasted
     */
    public void broadcastResponse(Response r) {
        for(Player player : players) {
            player.sendResponse(r);
        }
    }

    public void broadcastMessage(String message) {
        for(Player player : players) {
            player.sendMessage(message);
        }
    }

    //All Setter/Getter methods BELOW here.

    /**
     * Get a Player by their address. Be warned as with different protocols there could be
     * two players playing from the same address.
     * @param address The SocketAddress where the player is connecting from
     * @return The Player, if found, null if not
     */
    public Player getPlayer(SocketAddress address) {
        for(Player player : players) {
            if(player.getAddress().toString().equals(address.toString())) {
                return player;
            }
        }
        return null;
    }

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

    public List<Player> getPlayers() {
        return players;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public ServerConfig getConfig() {
        return config;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    protected int getNextEntityID() {
        return nextEntityID++;
    }
}
