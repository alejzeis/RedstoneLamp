package redstonelamp.plugin;

import redstonelamp.RedstoneLamp;
import redstonelamp.Server;

public class PluginBase {
	public void onLoad() {}
	
	public void onEnable() {}
	
	public Server getServer() {
		return RedstoneLamp.server;
	}
}
