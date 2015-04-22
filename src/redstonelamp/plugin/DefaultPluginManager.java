package redstonelamp.plugin;

import java.io.File;
import java.util.ArrayList;

public class DefaultPluginManager {
	private ArrayList<Plugin> pluginList = new ArrayList<Plugin>();
	
	public void registerInterfaces(Class<? extends PluginLoader> loader) {
		if(!PluginLoader.class.isAssignableFrom(loader))
			throw new IllegalArgumentException(String.format("Loader %s does not implement PluginLoader interface.", loader.getName()));
	}
	
	public ArrayList<Plugin> loadPlugin(File file) {
		return pluginList;
	}
}
