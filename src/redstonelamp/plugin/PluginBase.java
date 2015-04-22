package redstonelamp.plugin;

import java.util.ArrayList;
import java.util.List;

import redstonelamp.RedstoneLamp;
import redstonelamp.Server;
import redstonelamp.cmd.CommandRegistrationManager;
import redstonelamp.cmd.PluginCommand;
import redstonelamp.logger.Logger;

public abstract class PluginBase implements Plugin {
	private boolean enabled;
	private boolean initialzed = false;
	private PluginDescription description;
	private String dataFolder, file;
	private Logger logger;
	private PluginLoader loader;
	private Server server;
	private String name;
	
	public void onLoad() {}
	
	public void onEnable() {}
	
	public void onDisable() {}
	
	@Override
	public boolean isEnabled() {
		return (enabled == true);
	}
	
	public void setEnabled(final boolean enabled) {
		if(this.enabled != enabled) {
			this.enabled = enabled;
			if(this.enabled)
				onEnable();
			else
				onDisable();
		}
	}
	
	@Override
	public boolean isDisabled() {
		return enabled == false;
	}
	
	@Override
	public String getDataFolder() {
		return this.dataFolder;
	}
	
	@Override
	public PluginDescription getDescription() {
		return this.description;
	}
	
	public void init(PluginLoader loder, Server server, PluginDescription description, final String datafolder, final String file) {
		if(this.initialzed == false) {
			this.initialzed = true;
			this.loader = loader;
			this.server = server;
			this.description = description;
			this.logger = new Logger();
			this.dataFolder = dataFolder;
			this.file = file;
			System.out.println(" .... inside init method.....");
		}
	}
	
	public boolean isInitialized() {
		return this.initialzed;
	}
	
	public PluginCommand getCommand(final String name) {
		PluginCommand command = (PluginCommand) RedstoneLamp.server.getCommandRegistrationManager().getPluginCommand(name);
		return command;
	}
	
	public Server getServer() {
		return RedstoneLamp.server;
	}
	
	public String getBaseFolderName() {
		return null;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	public String getFile() {
		return file;
	}
	
	@Override
	public PluginLoader getPluginLoader() {
		return loader;
	}
	
	@Override
	public String getResource(String file) {
		return null;
	}
	
	@Override
	public ArrayList<String> getResources() {
		return null;
	}
	
	public CommandRegistrationManager getCommandRegistrationManager() {
		return getServer().getCommandRegistrationManager();
	}
	
	public List<String> getCommands() {
		return null;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
}
