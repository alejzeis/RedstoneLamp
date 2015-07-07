package redstonelamp;

import java.io.*;
import java.util.Properties;

import redstonelamp.utils.MainLogger;

public class RedstoneLamp implements Runnable{
	private static Server SERVER_INSTANCE;
	private MainLogger logger = new MainLogger();
	
	public static void main(String[] args) {
		new RedstoneLamp().run();
	}

	public void run(){
		try {
			Properties properties = loadProperties();
			SERVER_INSTANCE = new Server(properties, logger);
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
			properties.put("interface", "0.0.0.0");
			properties.put("port", "19132");
			properties.put("debug", "false");
			properties.store(new FileWriter(propFile), "RedstoneLamp properties");
		}
		properties.load(new FileReader(propFile));
		return properties;
	}

	public static Server getServerInstance(){
		return SERVER_INSTANCE;
	}

}
