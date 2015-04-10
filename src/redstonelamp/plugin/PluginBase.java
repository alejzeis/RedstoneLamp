package redstonelamp.plugin;

import redstonelamp.RedstoneLamp;
import redstonelamp.Server;

public class PluginBase {
	/*
	 * Do something when the plugin is loading
	 */
	public void onLoad() {}
	
	/*
	 * Do something when the plugin is enabled
	 */
	public void onEnable() {}
	
	/*
	 * Returns the Server instance
	 */
	public Server getServer() {
		return RedstoneLamp.server;
	}
	
	/*
	 * Do something when the plugin is disabled
	 */
	public void onDisable() {}
}
