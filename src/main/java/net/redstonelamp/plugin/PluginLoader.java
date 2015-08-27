package net.redstonelamp.plugin;

import java.io.InputStream;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

public abstract class PluginLoader {
	public enum PluginState{
		UNLOADED,
		LOADED,
		INITIALIZED,
		ENABLED,
		DISABLED
	}
	/**
	 * Current state this loader is in
	 */
	@Getter @Setter private PluginState state = PluginState.UNLOADED;
	/**
	 * Name of plugin
	 */
	@Getter protected String name = null;
	/**
	 * Version of plugin
	 */
	@Getter protected String version = null;
	/**
	 * The plugin manager handling this loader
	 */
	@Getter private PluginManager pluginManager;
	/**
	 * Instance of plugin
	 */
	@Getter protected Plugin plugin = null;
	/**
	 * List of dependencies that are needed to run this plugin
	 */
	@Getter private ArrayList<String> dependencies = new ArrayList<>();
	/**
	 * List of dependencies that are optional to influence behaviour of this plugin
	 */
	@Getter private ArrayList<String> softDependencies = new ArrayList<>();
	
	public PluginLoader(PluginManager mgr){
		this.pluginManager = mgr;
	}
	
	/**
	 * Load the plugin
	 */
	public abstract void loadPlugin();
	/**
	 * Initialize the plugin
	 */
	public abstract void initPlugin();
	/**
	 * Enable the plugin
	 */
	public abstract void enablePlugin();
	/**
	 * Disable the plugin
	 */
	public abstract void disablePlugin();

	/**
	 * Returns if this loader's plugin depends on the dependency
	 * @param depend The dependency to check
	 * @return
	 */
	public boolean dependsOn(String depend) {
		for(String dependency : getDependencies()){
			if(dependency.equalsIgnoreCase(depend))return true;
		}
		return false;
	}
	/**
	 * Returns if this loader's plugin soft depends on the dependency
	 * @param depend The dependency to check
	 * @return
	 */
	public boolean softDependsOn(String depend) {
		for(String dependency : getSoftDependencies()){
			if(dependency.equalsIgnoreCase(depend))return true;
		}
		return false;
	}
	
	/**
	 * Gets a resource as input stream
	 * @param String path: Path to the resource requested
	 * @throws Exception 
	 * @returns InputStream
	 */
	public abstract InputStream getResourceAsStream(String path) throws Exception;
}
