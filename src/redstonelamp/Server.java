package redstonelamp;

import redstonelamp.network.JRakLibInterface;
import redstonelamp.utils.MainLogger;

import java.util.Properties;

public class Server implements Runnable{
    private boolean debugMode = false;
    private MainLogger logger;
    private Properties properties;

    private String bindInterface;
    private int bindPort;

    private JRakLibInterface rakLibInterface;

    public Server(Properties properties, MainLogger logger){
        this.logger = logger;
        this.properties = properties;
        debugMode = Boolean.parseBoolean(properties.getProperty("debug", "false"));
        bindInterface = properties.getProperty("interface", "0.0.0.0");
        bindPort = Integer.parseInt(properties.getProperty("port", "19132"));
        rakLibInterface = new JRakLibInterface(this);
    }

    @Override
    public void run(){
        tick();
    }

    /**
     * Executes a server tick.
     */
    private void tick() {

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
}
