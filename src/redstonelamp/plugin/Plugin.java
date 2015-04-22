package redstonelamp.plugin;

import java.util.ArrayList;

import redstonelamp.Server;
import redstonelamp.cmd.CommandExecutor;
import redstonelamp.logger.Logger;

public interface Plugin extends CommandExecutor {
	public void onLoad();
	
	public void onEnable();
	
	public void onDisable();
	
	public PluginDescription getDescription();
	
	public String getDataFolder();
	
	public String getResource(String file);
	
	public ArrayList<String> getResources();
	
	public Server getServer();
	
	public String getName();
	
	public PluginLoader getPluginLoader();
	
	public boolean isEnabled();
	
	public boolean isDisabled();
}
