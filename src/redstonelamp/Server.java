package redstonelamp;

import redstonelamp.network.JRakLibInterface;
import redstonelamp.network.Network;
import redstonelamp.plugin.PluginManager;
import redstonelamp.utils.MainLogger;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Server implements Runnable{
    private boolean debugMode = false;
    private MainLogger logger;
    private Properties properties;

    private boolean running = false;

    private String bindInterface;
    private int bindPort;

    private List<Player> players = new ArrayList<>();
    private Network network;
    
    private PluginManager pluginManager;

    public Server(Properties properties, MainLogger logger){
        this.logger = logger;
        this.properties = properties;
        debugMode = Boolean.parseBoolean(properties.getProperty("debug", "false"));
        bindInterface = properties.getProperty("server-ip", "0.0.0.0");
        bindPort = Integer.parseInt(properties.getProperty("port", "19132"));

        logger.info("This server is running " + RedstoneLamp.SOFTWARE + " version " + RedstoneLamp.VERSION + " \"" + RedstoneLamp.CODENAME + "\" (API " + RedstoneLamp.API_VERSION + ")");
        logger.info(RedstoneLamp.SOFTWARE + " is distributed under the " + RedstoneLamp.LICENSE);
        
        network = new Network(this);
        network.registerInterface(new JRakLibInterface(this));

        RedstoneLamp.setServerInstance(this);
        pluginManager = new PluginManager();

        pluginManager.getPluginLoader().loadPlugins();
        logger.info("Done! Type \"help\" or \"?\" for help.");
        
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
            }
        }
    }

    /**
     * Executes a server tick.
     */
    private void tick() {
        network.tick();
    }

    public void addPlayer(Player player){
        synchronized (players){
            players.add(player);
        }
    }

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

    public Properties getProperties() {
        return properties;
    }

    public MainLogger getLogger() {
        return logger;
    }

    public String getBindInterface() {
        return bindInterface;
    }

    public int getBindPort() {
        return bindPort;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public Network getNetwork() {
        return network;
    }

    public List<Player> getOnlinePlayers() {
        return players;
    }
    
    public PluginManager getPluginManager() {
    	return pluginManager;
    }
}
