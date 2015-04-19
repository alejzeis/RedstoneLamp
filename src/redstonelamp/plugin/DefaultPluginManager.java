package redstonelamp.plugin;

import java.io.File;
import java.util.ArrayList;

public class DefaultPluginManager {
	
 /*
  * This class is responsible for loading plug-ins and invoked
  * from Server class
  */
	
	private ArrayList<Plugin> pluginList = new ArrayList<Plugin>();
	
	public void registerInterfaces(Class<? extends PluginLoader> loader) {
		///// throws exception if plug-in is not implementing PluginLoader interface.
		if(!PluginLoader.class.isAssignableFrom(loader)) throw new IllegalArgumentException(String.format("Loader %s does not implement PluginLoader interface.", loader.getName()));
	}
	/*
	 * loads all plug-ins that are required by the server
	 */
	public ArrayList<Plugin> loadPlugin(File file) {
		return pluginList;
	}
	
	
}
