package redstonelamp.plugin;

import redstonelamp.RedstoneLamp;
import redstonelamp.Server;
import redstonelamp.utils.MainLogger;

public class PluginBase implements Plugin {
	public void onLoad() {}
	
	public void onEnable() {}
	
	public void onDisable() {}
	
	public Server getServer() {
		return RedstoneLamp.getServerInstance();
	}
	
	public MainLogger getLogger() {
		return this.getServer().getLogger();
	}
}