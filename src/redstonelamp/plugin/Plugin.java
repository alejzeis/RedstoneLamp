package redstonelamp.plugin;

import java.io.File;

import redstonelamp.Server;
import redstonelamp.utils.MainLogger;

public interface Plugin {
	public void onLoad();
	
	public void onEnable();
	
	public void onDisable();
	
	public Server getServer();
	
	public MainLogger getLogger();
	
	public File getDataFolder();
}