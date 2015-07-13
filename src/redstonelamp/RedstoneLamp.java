package redstonelamp;

import java.io.*;
import java.util.Properties;

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
	private MainLogger logger = new MainLogger();
	
	public static void main(String[] args) {
		new RedstoneLamp().run();
	}

	public void run(){
		try {
			Properties properties = loadProperties();
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
			//Put default values
			properties.put("motd", "Minecraft: PE Server");
			properties.put("server-ip", "0.0.0.0");
			properties.put("port", "19132");
			properties.put("debug", "false");
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

}
