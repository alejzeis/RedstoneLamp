package redstonelamp.plugin;

import redstonelamp.RedstoneLamp;

public class PluginLoader {
	public void loadPlugin(String plugin) {
		RedstoneLamp.server.getLogger().info("Unable to load plugin " + plugin + " (Plugins not supported)");
	}
}
