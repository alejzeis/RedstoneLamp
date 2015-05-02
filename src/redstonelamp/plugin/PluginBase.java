package redstonelamp.plugin;

import java.util.ArrayList;
import java.util.List;

import redstonelamp.RedstoneLamp;
import redstonelamp.Server;
import redstonelamp.cmd.CommandRegistrationManager;
import redstonelamp.cmd.PluginCommand;

public abstract class PluginBase implements Plugin {
	private boolean enabled;
	private boolean initialzed = false;
	private PluginDescription description;
	private String dataFolder, file;
	private PluginLoader loader;
	private Server server;
	private String name;
	private double version;
	
	public void onLoad() {
	}
	
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
	
	public void init(PluginLoader loader, Server server, PluginDescription description, String dataFolder, String file) {
		if(this.initialzed == false) {
			this.initialzed = true;
			this.loader = loader;
			this.server = server;
			this.description = description;
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
		return server;
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
	
	public final void setAPIVersion(final double version) {
		if(version > RedstoneLamp.API_VERSION ) {
			throw new IllegalArgumentException(" Invalid Plug-in version. It has to be "+RedstoneLamp.API_VERSION);
		}else if(version < RedstoneLamp.API_VERSION) {
			getServer().getLogger().warn(" :Plug-in version needs to be "+RedstoneLamp.API_VERSION);
		}
		this.version = version;
	}
	
	public double getAPIVersion() {
		return version;
	}
}
