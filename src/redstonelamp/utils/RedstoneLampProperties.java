package redstonelamp.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import redstonelamp.RedstoneLamp;

public class RedstoneLampProperties {
	private static Properties properties;
	private static Properties redstonelampProperties;

	public String get(String property) {
		return properties.getProperty(property);
	}

	public String getRedstoneProperty(String property) {
		return redstonelampProperties.getProperty(property);
	}

	public void load() {
		try {
			properties = new Properties();
			RedstoneLamp.logger.info("Loading server properties...");
			InputStream is = RedstoneLamp.class.getClassLoader()
					.getResourceAsStream("server.properties");
			properties.load(is);
			RedstoneLamp.logger.info("server properties loaded...");

			// //// now load redstonelamp.properties file
			if (is != null)
				is.close();
			RedstoneLamp.logger.info("Loading redstonelamp properties...");
			redstonelampProperties = new Properties();
			is = RedstoneLamp.class.getClassLoader().getResourceAsStream(
					"redstonelamp.properties");
			redstonelampProperties.load(is);
			RedstoneLamp.logger.info("redstonelamp properties loaded...");
			if (is != null)
				is.close();
		} catch (IOException e) {
			if (RedstoneLamp.DEBUG)
				e.printStackTrace();
		}
	}
}
