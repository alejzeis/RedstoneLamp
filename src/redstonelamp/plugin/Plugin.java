package redstonelamp.plugin;

import java.util.ArrayList;

import redstonelamp.Server;
import redstonelamp.logger.Logger;

public interface Plugin {
	public PluginDescription getDescription();
	
	public String getDataFolder();
	
	public String getResource(String file);
	
	public ArrayList<String> getResources();
	
	public Server getServer();
	
	public String getName();
	
	public Logger getLogger();
	
	public PluginLoader getPluginLoader();
	
	public boolean isEnabled();
	
	public boolean isDisabled();
}