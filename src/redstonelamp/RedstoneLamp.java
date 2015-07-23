package redstonelamp;

import java.io.*;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import redstonelamp.cmd.defaults.Help;
import redstonelamp.cmd.defaults.Kick;
import redstonelamp.cmd.defaults.Stop;
import redstonelamp.utils.MainLogger;

public class RedstoneLamp implements Runnable{
	public static String MC_VERSION = "0.11.1";
	public static String SOFTWARE = "RedstoneLamp";
	public static String VERSION = "1.2.0";
	public static String CODENAME = "Snowball";
	public static String STAGE = "DEVELOPMENT";
	public static double API_VERSION = 1.4;
	public static String LICENSE = "GNU GENERAL PUBLIC LICENSE v3";
	
	private static Server SERVER_INSTANCE;
	private static ExecutorService async;
	private MainLogger logger = new MainLogger();
	
	public static void main(String[] args) {
		new RedstoneLamp().run();
	}

	public void run(){
		try {
			Properties properties = loadProperties();
			int workers = Integer.parseInt(properties.getProperty("async-workers", "4"));
			async = Executors.newFixedThreadPool(workers);
			new Server(properties, logger);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Properties loadProperties() throws IOException {
		logger.info("Loading server properties...");
		Properties properties = new Properties();
		File propFile = new File("server.properties");
		if(!propFile.exists()){
			propFile.createNewFile();
			properties.put("motd", "A Minecraft Server");
			properties.put("server-ip", "0.0.0.0");
			properties.put("max-players", "20");
			properties.put("white-list", "false");
			properties.put("level-name", "world");
			properties.put("level-type", "DEFAULT");
			properties.put("level-seed", "");
			properties.put("spawn-npcs", "true");
			properties.put("spawn-animals", "true");
			properties.put("spawn-monsters", "true");
			properties.put("hardcore", "false");
			properties.put("enable-rcon", "false");
			properties.put("force-gamemode", "false");
			properties.put("enable-query", "false");
			properties.put("allow-flight", "false");
			properties.put("announce-player-achievements", "true");
			properties.put("pvp", "true");
			properties.put("difficulty", "1");
			properties.put("gamemode", "0");
			properties.put("view-distance", "10");
			properties.put("generate-structures", "true");
			properties.put("port", "19132");
			properties.put("debug", "false");
			properties.put("async-workers", "4");
			properties.store(new FileWriter(propFile), "RedstoneLamp properties");
		}
		properties.load(new FileReader(propFile));
		return properties;
	}

	protected static void setServerInstance(Server server){
		RedstoneLamp.SERVER_INSTANCE = server;
	}

	public static Server getServerInstance(){
		return RedstoneLamp.SERVER_INSTANCE;
	}
	
	public static ExecutorService getAsync() {
		return async;
	}

	public static void registerDefaultCommands() {
		getServerInstance().getCommandManager().registerCommand("help", "Shows all available commands", new Help());
		getServerInstance().getCommandManager().registerCommand("kick", "Kicks a player from the server", new Kick());
		getServerInstance().getCommandManager().registerCommand("stop", "Stops the server", new Stop());
	}
}
