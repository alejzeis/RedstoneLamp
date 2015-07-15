package redstonelamp.plugin;

import java.io.File;

import redstonelamp.RedstoneLamp;
import redstonelamp.Server;
import redstonelamp.utils.MainLogger;

public class PluginBase implements Plugin {
	private String name;
	private String description;
	private double version;
	private String author;
	
	private double api;
	
	public void onLoad() {}
	
	public void onEnable() {}
	
	public boolean isEnabled() {
		return false;
	}
	
	public void onDisable() {}
	
	public boolean isDisabled() {
		return true;
	}
	
	public Object getDataFolder() {
		return null;
	}
	
	public void setDescription(final String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
	
	public Object getResource(String filename) {
		return null;
	}
	
	public Object getResource(String filename, boolean replace) {
		return null;
	}
	
	public Object getResources() {
		return null;
	}
	
	public Object getConfig() {
		return null;
	}
	
	public boolean saveConfig() {
		return false;
	}
	
	public boolean saveDefaultConfig() {
		File dir = new File("./plugins/" + this.getName());
		if(dir.isDirectory())
			dir.mkdirs();
		//TODO: Save config to dir
		return false;
	}
	
	public void reloadConfig() {
		//TODO: Reload the plugins config
	}
	
	public Server getServer() {
		return RedstoneLamp.getServerInstance();
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setAuthor(final String author) {
		this.author = author;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public void setVersion(final double version) {
		this.version = version;
	}
	
	public double getVersion() {
		return version;
	}
	
	public void setAPIVersion(final double api) {
		this.api = api;
	}
	
	public double getAPIVersion() {
		return api;
	}
	
	public MainLogger getLogger() {
		return RedstoneLamp.getServerInstance().getLogger();
	}
	
	public PluginLoader getPluginLoader() {
		return getServer().getPluginManager().getPluginLoader();
	}
}