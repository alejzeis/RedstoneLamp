package redstonelamp.plugin;

import java.util.ArrayList;

import redstonelamp.RedstoneLamp;
import redstonelamp.Server;
import redstonelamp.logger.PlugginLogger;


public abstract class PluginBase implements Plugin {
	
	private boolean enabled ;
	
	private boolean initialzed = false;
	
	private PluginDescription description;
	
	private String dataFolder;
	
	private String configFile;
	
	private PlugginLogger logger; 
	
	private PluginLoader loader;
	
	private Server server;

	private String file;
	
	private String name;
	
	/*
	 * Do something when the plugin is loading
	 */
	public void onLoad() {}
	
	/*
	 * Do something when the plugin is enabled
	 */
	public void onEnable() {}
	

	/*
	 * Do something when the plugin is disabled
	 */
	public void onDisable() {}
	
	@Override
	public boolean isEnabled() {
    	return (enabled == true);
	}
	
	public void setEnabled(final boolean enabled ) {
		if(this.enabled != enabled) {
			this.enabled = enabled;
			if(this.enabled) onEnable();
			else onDisable();
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

	public void init(final PluginLoader loder, Server server, PluginDescription description, final String datafolder, final String file) {
		if(this.initialzed == false) {
			this.initialzed  = true;
			this.loader      = loader;
			this.server      = server;
			this.description = description;
			this.logger      = new PlugginLogger();
			this.dataFolder  = dataFolder;
			this.file        = file;
		}
	}
	
	@Override
	public PlugginLogger getLogger() {
		return logger;
	}

	public boolean isInitialized() {
		return this.initialzed;
	}
	
	/*
	 * Returns the Server instance
	 */
	public Server getServer() {
		return RedstoneLamp.server;
	}

	public String getBaseFolderName() {
		return null;
	}
	
	@Override
	public String getName() {
		return this.description.getName();
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
	
	
}
