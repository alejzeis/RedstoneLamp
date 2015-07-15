package redstonelamp.plugin;

import redstonelamp.Server;
import redstonelamp.utils.MainLogger;

public interface Plugin {
	public void onLoad();
	
	public void onEnable();
	
	public boolean isEnabled();
	
	public void onDisable();
	
	public boolean isDisabled();
	
	public Object getDataFolder();
	
	public Object getDescription();

	public Object getResource(String filename);
	public Object getResource(String filename, boolean replace);
	
	public Object getResources();
	
	public Object getConfig();
	
	public boolean saveConfig();
	
	public boolean saveDefaultConfig();
	
	public void reloadConfig();
	
	public Server getServer();
	
	public Object getName();
	
	public MainLogger getLogger();
	
	public PluginLoader getPluginLoader();
}