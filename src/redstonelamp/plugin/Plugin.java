package redstonelamp.plugin;

import redstonelamp.Server;
import redstonelamp.utils.MainLogger;

public interface Plugin {
	public void onLoad();
	
	public void onEnable();
	
	public void onDisable();
	
	public Server getServer();
	
	public MainLogger getLogger();
}