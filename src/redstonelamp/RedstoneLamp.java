package redstonelamp;

import java.io.IOException;

import redstonelamp.utils.MainLogger;

public class RedstoneLamp {
	public static MainLogger logger = new MainLogger();
	public static boolean isDebugMode = false;
	
	public static void main(String[] args) {
		try {
			loadProperties();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void loadProperties() throws IOException {
		logger.info("Loading server properties...");
		//TODO: Properties
	}
}
