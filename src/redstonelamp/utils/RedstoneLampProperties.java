package redstonelamp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import redstonelamp.RedstoneLamp;

public class RedstoneLampProperties {
	private static Properties properties;
	
	public String get(String property) {
		return properties.getProperty(property);
	}
	
	public void load() {
		try {
			properties = new Properties();
			RedstoneLamp.logger.info("Loading server properties...");
			InputStream is = RedstoneLamp.class.getClassLoader().getResourceAsStream("server.properties");
			properties.load(is);
		} catch(IOException e) {
			if(RedstoneLamp.DEBUG)
				e.printStackTrace();
		}
	}
}
