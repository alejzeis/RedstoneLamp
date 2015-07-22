package redstonelamp.plugin;

import java.io.File;

import redstonelamp.Server;
import redstonelamp.utils.MainLogger;

public interface Plugin {
	public void onLoad();
	
	public void onEnable();
	
	public boolean isEnabled(String plugin);
	
	public void onDisable();
	
	public boolean isDisabled(String plugin);
	
	public Server getServer();
	
	public MainLogger getLogger();
	
	public File getDataFolder();
}