package redstonelamp.plugin;

import java.util.ArrayList;

import redstonelamp.plugin.js.JavaScriptManager;

public class PluginManager {
	private PluginLoader pluginLoader;
	private JavaScriptManager jsManager;
	private ArrayList<Object> plugins = new ArrayList<Object>();
	
	public PluginManager() {
		pluginLoader = new PluginLoader();
		jsManager = new JavaScriptManager();
	}
	
	public PluginLoader getPluginLoader() {
		return pluginLoader;
	}
	
	public ArrayList<Object> getPluginArray() {
		return this.plugins;
	}
	
	public JavaScriptManager getJavaScriptManager() {
		return this.jsManager;
	}
}
