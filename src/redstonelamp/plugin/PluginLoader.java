package redstonelamp.plugin;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;

import redstonelamp.RedstoneLamp;

public class PluginLoader {
	/*
	 * Loads a plugin by its name
	 */
	public void loadPlugin(String plugin) {
		try {
			URL classUrl;
			URL pluginURL = new File("./plugins/" + plugin).toURL();
			classUrl = new URL(pluginURL.toExternalForm());
			URL[] classUrls = { classUrl };
			URLClassLoader ucl = new URLClassLoader(classUrls);
			Class c = ucl.loadClass(plugin.substring(0, plugin.indexOf(".")));
			for(Field f: c.getDeclaredFields()) {
				System.out.println("Field name" + f.getName());
			}
		} catch(Exception e) {
			e.printStackTrace();
			RedstoneLamp.server.getLogger().info("Unable to load plugin " + plugin + " (Plugins not supported)");
		}
	}
}
