package redstonelamp.plugin;

import java.util.ArrayList;

public class PluginManager {
	

	private ArrayList<Plugin> plugins = new ArrayList<Plugin>();
	
	/*
	 * Register Plug-in loader class
	 */
	public void registerPluginLoader(Class<? extends PluginLoader> loader) {
	}
	
	public ArrayList<Plugin> getPlugins() {
		return plugins;
	}
}
