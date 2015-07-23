package redstonelamp;

import redstonelamp.cmd.CommandManager;
import redstonelamp.event.EventManager;
import redstonelamp.event.player.PlayerJoinEvent;
import redstonelamp.event.server.ServerStopEvent;
import redstonelamp.event.server.ServerTickEvent;
import redstonelamp.level.Level;
import redstonelamp.network.JRakLibInterface;
import redstonelamp.network.Network;
import redstonelamp.network.pc.PCInterface;
import redstonelamp.plugin.PluginManager;
import redstonelamp.utils.MainLogger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Server implements Runnable{
    private boolean debugMode = false;
    private String motd;
    private int maxPlayers;

    private MainLogger logger;
    private Properties properties;

    private boolean running = false;

    private String bindInterface;
    private int bindPort;

    private List<Player> players = new ArrayList<>();
    private Network network;
    private Level mainLevel;
    private BufferedReader cli;
    
    private PluginManager pluginManager;
    private EventManager eventManager;
    private CommandManager commandManager;

    public Server(Properties properties, MainLogger logger){
    	eventManager = new EventManager();
    	commandManager = new CommandManager();
        this.logger = logger;
        this.properties = properties;

        debugMode = Boolean.parseBoolean(properties.getProperty("debug", "false"));
        bindInterface = properties.getProperty("server-ip", "0.0.0.0");
        bindPort = Integer.parseInt(properties.getProperty("port", "19132"));
        motd = properties.getProperty("motd", "A Minecraft Server");
        maxPlayers = Integer.parseInt(properties.getProperty("max-players", "20"));

        logger.info("This server is running " + RedstoneLamp.SOFTWARE + " version " + RedstoneLamp.VERSION + " \"" + RedstoneLamp.CODENAME + "\" (API " + RedstoneLamp.API_VERSION + ")");
        logger.info(RedstoneLamp.SOFTWARE + " is distributed under the " + RedstoneLamp.LICENSE);

        mainLevel = new Level(this);
        
        network = new Network(this);
        network.registerInterface(new JRakLibInterface(this));
        network.registerInterface(new PCInterface(this));
        network.setName(motd);

        RedstoneLamp.setServerInstance(this);
        pluginManager = new PluginManager();

        pluginManager.getPluginLoader().loadPlugins();
        pluginManager.getPluginLoader().enablePlugins();
        RedstoneLamp.registerDefaultCommands();
        logger.info("Done! Type \"help\" for help.");
        cli = new BufferedReader(new InputStreamReader(System.in));
        
        running = true;
        run();
    }

    @Override
    public void run(){
        while(running){
            long start = Instant.now().toEpochMilli();
            tick();
            long diff = Instant.now().toEpochMilli() - start;
            if(diff < 50){
                long until = Instant.now().toEpochMilli() + (50 - diff);
                while(true) {
                    if (Instant.now().toEpochMilli() >= until) {
                        break;
                    }
                }
            } else {
                logger.warning(diff+">50 Did the system time change, or is the server overloaded?");
            }
        }
    }

    /**
     * Executes a server tick.
     */
    private void tick() {
        network.tick();
        mainLevel.tick();
     	getEventManager().getEventExecutor().execute(new ServerTickEvent());
		RedstoneLamp.getAsync().execute(() -> {
            String line = null;
            try {
                if(cli.ready()) {
                    line = cli.readLine();
                    if(line != null)
                        RedstoneLamp.getServerInstance().getCommandManager().getCommandExecutor().executeCommand(line, RedstoneLamp.getServerInstance());
                }
            } catch (IOException e) {}
        });
    }

    /**
     * Adds a player to the server
     * 
     * @param Player
     */
    public void addPlayer(Player player){
        synchronized (players){
            players.add(player);
            getEventManager().getEventExecutor().execute(new PlayerJoinEvent(player));
        }
    }

    /**
     * Gets a player from the server
     * 
     * @param String
     * @return Player
     */
    public Player getPlayer(String identifier){
        synchronized (players){
            for(Player player : players){
                if(player.getIdentifier().equals(identifier)){
                    return player;
                }
            }
        }
        return null;
    }

    /**
     * INTERNAL METHOD! Use <code>player.kick()</code> instead.
     * @param player
     */
    public void removePlayer(Player player){
        players.remove(player);
    }

    /**
     * Returns the server.properties data
     * 
     * @return Properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Returns the server logger
     * 
     * @return MainLogger
     */
    public MainLogger getLogger() {
        return logger;
    }

    /**
     * Returns the servers Bind Interface
     * 
     * @return String
     */
    public String getBindInterface() {
        return bindInterface;
    }

    /**
     * Returns the server port
     * 
     * @return int
     */
    public int getBindPort() {
        return bindPort;
    }

    /**
     * Returns true if the server is in DEBUG Mode
     * 
     * @return boolean
     */
    public boolean isDebugMode() {
        return debugMode;
    }

    /**
     * Returns the servers Network
     * 
     * @return Network
     */
    public Network getNetwork() {
        return network;
    }

    /**
     * Returns a List array of all online players
     * 
     * @return List<Player>
     */
    public List<Player> getOnlinePlayers() {
        return players;
    }
    
    /**
     * Returns the plugin manager
     * 
     * @return PluginManager
     */
    public PluginManager getPluginManager() {
    	return pluginManager;
    }
    
    /**
     * Returns the event manager
     * 
     * @return EventManager
     */
    public EventManager getEventManager() {
    	return eventManager;
    }
    
    public CommandManager getCommandManager() {
    	return commandManager;
    }

    /**
     * Returns the servers MOTD
     * 
     * @return String
     */
    public String getMotd() {
        return motd;
    }

    /**
     * Returns the max number of players that can join
     * 
     * @return int
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Returns the default server level
     * 
     * @return Level
     */
    public Level getMainLevel(){
        return mainLevel;
    }
    
    /**
     * Stops the server
     */
    public void stop() {
    	getEventManager().getEventExecutor().execute(new ServerStopEvent());
    	try {
    		if(cli instanceof BufferedReader)
    			cli.close();
		} catch (IOException e) {}
		for(Player p : getOnlinePlayers()) {
			p.kick("Server closed.", false);
		}
        mainLevel.shutdown();
    	pluginManager.getPluginLoader().disablePlugins();
        network.shutdown();
    	RedstoneLamp.getAsync().shutdown();
    	System.exit(0);
    }
}
