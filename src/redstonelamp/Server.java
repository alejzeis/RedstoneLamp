package redstonelamp;

import redstonelamp.cmd.CommandManager;
import redstonelamp.event.Event;
import redstonelamp.event.EventManager;
import redstonelamp.event.Listener;
import redstonelamp.event.player.PlayerJoinEvent;
import redstonelamp.event.player.PlayerQuitEvent;
import redstonelamp.event.server.ServerStopEvent;
import redstonelamp.event.server.ServerTickEvent;
import redstonelamp.io.playerdata.GenericPlayerDatabase;
import redstonelamp.io.playerdata.PlayerDatabase;
import redstonelamp.item.Item;
import redstonelamp.level.Level;
import redstonelamp.level.provider.FakeLevelProvider;
import redstonelamp.network.JRakLibInterface;
import redstonelamp.network.Network;
import redstonelamp.network.pc.PCInterface;
import redstonelamp.plugin.PluginManager;
import redstonelamp.utils.MainLogger;
import redstonelamp.utils.ServerIcon;
import redstonelamp.utils.TextFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server implements Runnable {
    private boolean debugMode = false;
    private String motd;
    private ServerIcon icon;
    private int maxPlayers;

    private MainLogger logger;
    private Properties properties;

    private boolean running = false;

    private String bindInterface;
    private int bindPort;

    private List<Player> players = new CopyOnWriteArrayList();
    private Network network;
    private Level mainLevel;
    private PlayerDatabase playerDatabase;
    private BufferedReader cli;
    
    private PluginManager pluginManager;
    private EventManager eventManager;
    private CommandManager commandManager;
    private boolean shuttingDown = false;

    private int nextEntityID = 0;
    private File playerDatbaseLocation;

    private boolean onlineMode = false;

    public Server(Properties properties, MainLogger logger){
    	eventManager = new EventManager();
    	commandManager = new CommandManager();
        this.logger = logger;
        this.properties = properties;
        bindInterface = properties.getProperty("server-ip", "0.0.0.0");
        bindPort = Integer.parseInt(properties.getProperty("mcpe-port", "19132"));
        motd = properties.getProperty("motd", "A Minecraft Server");
        try {
            File ficon = new File("server-icon.png");
            if(ficon.exists())
        	    icon = new ServerIcon(ficon);
            else
                icon = new ServerIcon(new File(getClass().getResource("/resources/img/server-icon.png").toURI()));
        } catch(Exception e) {
        	e.printStackTrace();
        }
        maxPlayers = Integer.parseInt(properties.getProperty("max-players", "20"));


        logger.info("This server is running " + RedstoneLamp.SOFTWARE + " version " + RedstoneLamp.VERSION + " \"" + RedstoneLamp.CODENAME + "\" (API " + RedstoneLamp.API_VERSION + ")");
        logger.info(RedstoneLamp.SOFTWARE + " is distributed under the " + RedstoneLamp.LICENSE);

        RedstoneLamp.setServerInstance(this);

        mainLevel = new Level(this);
        if(mainLevel.getProvider() instanceof FakeLevelProvider){
            logger.warning("This server is using a FakeLevelProvider, world changes and spawn positions are not saved.");
        }
        Item.init();
        logger.debug("Items initialized ("+Item.getCreativeItems().size()+" creative items)");

        try {
            playerDatbaseLocation = new File("players.dat");
            playerDatabase = new GenericPlayerDatabase();
            if(!playerDatbaseLocation.exists()){
                playerDatbaseLocation.createNewFile();
            } else {
                playerDatabase.loadFromFile(getPlayerDatbaseLocation());
            }
        } catch (IOException e) {
            logger.error("FAILED TO LOAD PLAYER DATABASE!!! java.io.IOException: "+e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        network = new Network(this);
        network.registerInterface(new JRakLibInterface(this));
        network.registerInterface(new PCInterface(this));
        network.setName(motd);

        pluginManager = new PluginManager();

        pluginManager.getPluginLoader().loadPlugins();
        pluginManager.getPluginLoader().enablePlugins();
        RedstoneLamp.registerDefaultCommands();

        eventManager.registerEvents(new InternalListener(this));

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
        try {
            network.tick();
            mainLevel.tick();
            getEventManager().getEventExecutor().execute(new ServerTickEvent());
            RedstoneLamp.getAsync().execute(() -> {
                String line = null;
                try {
                    if (cli.ready()) {
                        line = cli.readLine();
                        if (line != null)
                            RedstoneLamp.getServerInstance().getCommandManager().getCommandExecutor().executeCommand(line, RedstoneLamp.getServerInstance());
                    }
                } catch (IOException e) {}
            });
        } catch(Exception e){
            logger.warning("Exception in tick: "+e.getMessage());
            e.printStackTrace(System.err);
        }
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
    
	public void broadcast(String message) {
		logger.noTag(message);
		for(Player p : getOnlinePlayers()) {
			p.sendMessage(message);
		}
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
     * Returns the server icon
     * 
     * @return ServerIcon
     */
    public ServerIcon getIcon() {
    	return icon;
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
     * INTERNAL USE ONLY!
     * <br>
     * Returns the next entityID.
     * @return nextEntityID.
     */
    public int getNextEntityId() { //TODO: move to new EntityManager
        return nextEntityID++;
    }

    /**
     * Get the PlayerDatabase implementation for this server.
     * @return The PlayerDatabase used by the server.
     */
    public PlayerDatabase getPlayerDatabase() {
        return playerDatabase;
    }

    /**
     * Saves the server's PlayerDatabase.
     */
    public void savePlayerDatabase() {
        try {
            playerDatabase.save(getPlayerDatbaseLocation());
        } catch (IOException e) {
            getLogger().warning("Failed to save PlayerDatabase! java.io.IOException: "+e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Stops the server
     */
    public void stop() {
        shuttingDown = true;
    	logger.info("Stopping the server...");
    	getEventManager().getEventExecutor().execute(new ServerStopEvent());
    	try {
    		if(cli instanceof BufferedReader)
    			cli.close();
		} catch (IOException e) {}
		for(Player p : getOnlinePlayers()) {
			p.kick("Server closed.", true);
		}
        mainLevel.shutdown();
    	pluginManager.getPluginLoader().disablePlugins();
        network.shutdown();
    	logger.close();
    	RedstoneLamp.getAsync().shutdown();
    	System.exit(0);
    }

    public boolean isShuttingDown() {
        return shuttingDown;
    }

    public File getPlayerDatbaseLocation() {
        return playerDatbaseLocation;
    }

    public boolean isOnlineMode() {
        return onlineMode;
    }
}
