package redstonelamp.plugin;

import java.util.ArrayList;

public class PluginManager {
	private PluginLoader pluginLoader;
	private ArrayList<Object> plugins = new ArrayList<Object>();
	
	public PluginManager() {
		pluginLoader = new PluginLoader();
	}
	
	public PluginLoader getPluginLoader() {
		return pluginLoader;
	}
	
	public ArrayList<Object> getPluginArray() {
		return this.plugins;
	}
}
