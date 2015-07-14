package redstonelamp.plugin;

public class PluginManager {
	private PluginLoader pluginLoader;
	
	public PluginManager() {
		pluginLoader = new PluginLoader();
	}
	
	public PluginLoader getPluginLoader() {
		return pluginLoader;
	}
}
