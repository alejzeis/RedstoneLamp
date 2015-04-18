package redstonelamp.plugin;

import java.util.ArrayList;
import java.util.List;

import redstonelamp.RedstoneLamp;
import redstonelamp.Server;
import redstonelamp.cmd.Command;
import redstonelamp.cmd.CommandRegistrationManager;
import redstonelamp.logger.Logger;

public abstract class PluginBase implements Plugin {
	private boolean enabled;
	private boolean initialized = false;
	private PluginDescription description;
	private String dataFolder, file;
	private Logger logger;
	private PluginLoader loader;
	private Server server;
	private String name;
	
	/*
	 * Do something when the plug-in is loading
	 */
	public void onLoad() {
	}

	/*
	 * Do something when the plug-in is enabled
	 */
	public void onEnable() {
	}

	/*
	 * Do something when the plug-in is disabled
	 */
	public void onDisable() {
	}

	@Override
	public boolean isEnabled() {
		return (enabled == true);
	}

	/*
	 * @param boolean enabled
	 */
	public void setEnabled(final boolean enabled) {
		if (this.enabled != enabled) {
			this.enabled = enabled;
			if (this.enabled)
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

	public void init( PluginLoader loder, Server server, PluginDescription description, final String datafolder, final String file) {
		if (this.initialized == false) {
			this.initialized = true;
			this.loader = loader;
			this.server = server;
			this.description = description;
			this.logger = new Logger();
			this.dataFolder = dataFolder;
			this.file = file;
			System.out.println(" .... inside init method.....");
		}
	}

	@Override
	public Logger getLogger() {
		return logger;
	}

	/*
	 * @return boolean initialized
	 */
	public boolean isInitialized() {
		return this.initialized;
	}

	/*
	 * @param String name
	 * 
	 * @return Command
	 */
	public Command getCommand(final String name) {
		Command command = this.getCommandRegistrationManager().getPluginCommand(name);
		return command;
	}
	
	/*
	 * @return Server
	 */
	public Server getServer() {
		return RedstoneLamp.server;
	}

	/*
	 * @return String BaseFolder
	 */
	public String getBaseFolderName() {
		return null;
	}

	@Override
	public String getName() {
		return this.name;
	}

	/*
	 * @return String file
	 */
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
	
	/*
	 * @return CommandRegistrationManager
	 */
	public CommandRegistrationManager getCommandRegistrationManager() {
	   return getServer().getCommandRegistrationManager();
	}
	
	/*
	 * @return List<String> commands
	 */
	public List<String> getCommands(){
		return null;
	}

	/*
	 * @param String name
	 */
	public void setName(final String name){
		this.name = name;
	}
}
