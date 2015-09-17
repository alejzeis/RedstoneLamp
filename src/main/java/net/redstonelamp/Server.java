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
package net.redstonelamp;

import java.io.File;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import lombok.Getter;
import net.redstonelamp.config.ServerConfig;
import net.redstonelamp.config.YamlConfig;
import net.redstonelamp.level.Level;
import net.redstonelamp.level.LevelManager;
import net.redstonelamp.network.NetworkManager;
import net.redstonelamp.network.Protocol;
import net.redstonelamp.network.pc.PCProtocol;
import net.redstonelamp.network.pe.PEProtocol;
import net.redstonelamp.plugin.PluginSystem;
import net.redstonelamp.request.LoginRequest;
import net.redstonelamp.response.ChatResponse;
import net.redstonelamp.response.Response;
import net.redstonelamp.script.ScriptManager;
import net.redstonelamp.ticker.RedstoneTicker;
import net.redstonelamp.ui.Log4j2ConsoleOut;
import net.redstonelamp.ui.Logger;

/**
 * The base RedstoneLamp server, which handles the ticker.
 *
 * @author RedstoneLamp Team
 */
public class Server implements Runnable{
    private final List<Runnable> shutdownTasks = new ArrayList<>(); //List of tasks to be run on shutdown
    private final Logger logger;
    private final ServerConfig config;
    private final YamlConfig yamlConfig;
    private final RedstoneTicker ticker;
    private final NetworkManager network;
    private final List<Player> players = new CopyOnWriteArrayList<>();
    private final PluginSystem pluginSystem;
    @Getter private final ScriptManager scriptManager;
    private final PlayerDatabase playerDatabase;
    
    private String motd;
    private int maxPlayers;
    private LevelManager levelManager;
    private int nextEntityID = 1;

    private boolean stopped = false;

    /**
     * Package-private constructor used by the RedstoneLamp run class
     *
     * @param logger           The server's logger
     * @param config           The server's configuration
     * @param serverYamlConfig The server's YAML configuration
     */
    Server(Logger logger, ServerConfig config, YamlConfig serverYamlConfig){
        ticker = new RedstoneTicker(this, 50);
        this.logger = logger;
        this.config = config;
        yamlConfig = serverYamlConfig;
        network = new NetworkManager(this);
        loadProperties(config);
        logger.info(RedstoneLamp.getSoftwareVersionString() + " is licensed under the Lesser GNU General Public License version 3");
        logger.info("Build Information: " + RedstoneLamp.getBuildInformation());

        network.registerProtocol(new PEProtocol(network));
        network.registerProtocol(new PCProtocol(network));
        
        scriptManager = new ScriptManager(this);
        
        pluginSystem = new PluginSystem();
        pluginSystem.init(this, new Logger(new Log4j2ConsoleOut("PluginSystem")));
        pluginSystem.loadPlugins();
        pluginSystem.enablePlugins();
        
        scriptManager.initScriptAPI();
        scriptManager.loadScripts();

        network.setName(motd); //Set the name after plugins, as some plugins may add protocols

        levelManager = new LevelManager(this);
        levelManager.init();

        ticker.addDelayedRepeatingTask(tick -> levelManager.getLevels().stream().forEach(Level::save), 40, serverYamlConfig.getInt("settings.world-save-interval") * 20);
        addShutdownTask(() -> {
            logger.info("Saving levels...");
            levelManager.getLevels().stream().forEach(Level::save);
            logger.info("All levels saved.");
        });

        logger.info("Loading player data...");
        playerDatabase = new SimplePlayerDatabase(this); //TODO: Correct database
        try{
            playerDatabase.loadFrom(new File("players.dat"));
            ticker.addDelayedRepeatingTask(tick -> {
                try{
                    playerDatabase.saveTo(new File("players.dat"));
                }catch(IOException e){
                    logger.warning("Exception while auto-saving PlayerDatabase: " + e.getClass().getName() + ": " + e.getMessage());
                }
            }, 40, serverYamlConfig.getInt("players.playerdata-save-interval") * 20);
        }catch(IOException e){
            logger.fatal("FAILED TO LOAD PLAYER DATABASE! " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        logger.debug("Load complete.");

        addShutdownTask(pluginSystem::disablePlugins);
        addShutdownTask(() -> {
            try{
                playerDatabase.saveTo(new File("players.dat"));
            }catch(IOException e){
                logger.fatal("FAILED TO SAVE PLAYER DATABASE! " + e.getClass().getName() + ": " + e.getMessage());
            }
        });

        Runtime.getRuntime().addShutdownHook(new ShutdownTaskExecuter(this));
    }

    private void loadProperties(ServerConfig config){
        maxPlayers = config.getInt("max-players");
        motd = config.getString("motd");
    }

    @Override
    public void run(){
        logger.info(RedstoneLamp.SOFTWARE + " is now running.");
        ticker.start();
    }

    /**
     * Add a <code>Runnable</code> to be ran when the server begins to shutdown. This method is thread-safe.
     *
     * @param r The <code>Runnable</code> to be ran when the server stops.
     */
    public synchronized void addShutdownTask(Runnable r){
        synchronized(shutdownTasks){
            shutdownTasks.add(r);
        }
    }

    /**
     * INTERNAL METHOD
     *
     * @param address
     * @param protocol
     * @param loginRequest
     * @return
     */
    public Player openSession(SocketAddress address, Protocol protocol, LoginRequest loginRequest){
        logger.debug("Opened Session: " + address.toString());
        Player player = new Player(protocol, address);
        players.add(player);
        network.setName(motd); //Update the amount of players online
        return player;
    }

    /**
     * INTERNAL METHOD!
     *
     * @param player
     */
    public void closeSession(Player player){
        logger.debug("Closed Session: " + player.getAddress().toString());
        players.remove(player);
        network.setName(motd); //Update the amount of players online
    }

    /**
     * Broadcasts a response to ALL players on the server. Please use API methods if
     * available.
     *
     * @param r The Request to be broadcasted
     */
    public void broadcastResponse(Response r){
        for(Player player : players){
            player.sendResponse(r);
        }
    }

    /**
     * Broadcasts a response to a <code>Stream</code> of players.
     *
     * @param players A <code>Stream</code> of players for which the response will be
     *                broadcasted to.
     * @param r       The Response to be broadcasted.
     */
    public void broadcastResponse(Stream<Player> players, Response r){
        players.forEach(player -> player.sendResponse(r));
    }

    /**
     * Broadcasts an array of responses to all players on the server. This method
     * attempts to combine the responses into one or more packets, depending on each
     * player's protocol.
     *
     * @param responses The responses to be broadcasted.
     */
    public void broadcastResponses(Response[] responses){
        players.forEach(player -> player.getProtocol().sendQueuedResponses(responses, player));
    }

    public void broadcastMessage(String message){
        logger.info("[Chat]: " + message);
        for(Player player : players){
            player.sendMessage(message);
        }
    }

    public void broadcastMessage(ChatResponse.ChatTranslation translation){
        logger.info("[Chat]: " + translation.message.replaceAll("%", "") + " " + Arrays.toString(translation.params));
        ChatResponse cr = new ChatResponse(translation.message);
        cr.translation = translation;
        for(Player player : players){
            player.sendResponse(cr);
        }
    }

    public void savePlayerData(){
        try{
            playerDatabase.saveTo(new File("players.dat"));
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //All Setter/Getter methods BELOW here.

    /**
     * Get a Player by their address. Be warned as with different protocols there could be
     * two players playing from the same address.
     *
     * @param address The SocketAddress where the player is connecting from
     * @return The Player, if found, null if not
     */
    public Player getPlayer(SocketAddress address){
        for(Player player : players){
            if(player.getAddress().toString().equals(address.toString())){
                return player;
            }
        }
        return null;
    }

    /**
     * Retrieve the server's logger.
     *
     * @return The <code>Logger</code> instance the server uses.
     */
    public Logger getLogger(){
        return logger;
    }

    public RedstoneTicker getTicker(){
        return ticker;
    }

    public List<Player> getPlayers(){
        return players;
    }

    public int getMaxPlayers(){
        return maxPlayers;
    }

    public ServerConfig getConfig(){
        return config;
    }

    public LevelManager getLevelManager(){
        return levelManager;
    }

    public PluginSystem getPluginSystem(){
        return pluginSystem;
    }

    protected int getNextEntityID(){
        return nextEntityID++;
    }

    public YamlConfig getYamlConfig(){
        return yamlConfig;
    }

    public boolean isStopped(){
        return stopped;
    }

    public PlayerDatabase getPlayerDatabase(){
        return playerDatabase;
    }

    private static class ShutdownTaskExecuter extends Thread{
        private final Server server;

        public ShutdownTaskExecuter(Server server){
            this.server = server;
        }

        @Override
        public void run(){
            if(!server.isStopped()){
                server.getLogger().warning("Server is not shut-down, did the server crash!?");
            }
            server.getLogger().info("Running shutdown tasks!");
            server.shutdownTasks.forEach(Runnable::run);
            server.getLogger().info("Shutdown tasks complete! Halting...");
        }
    }
}
