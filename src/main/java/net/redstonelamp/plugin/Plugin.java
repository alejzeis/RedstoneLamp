package net.redstonelamp.plugin;

import lombok.Getter;

public abstract class Plugin {
	/**
	 * The plugins name
	 */
	@Getter private String name;
	/**
	 * The plugins version
	 */
	@Getter private String version;
	/**
	 * The plugins authors
	 */
	@Getter private String[] authors;
	/**
	 * The plugins website
	 */
	@Getter private String website;
	
	public Plugin(String name, String version, String[] authors, String website){
		this.name = name;
		this.version = version;
		this.authors = authors;
		this.website = website;
	}
	/**
	 * In this function every plugin should handle the stuff it has to do on server start.
	 * When this method gets called, the basic structure of the server already got loaded,
	 * and events and commands can already be registered in the event/command system.
	 * WARNING: This method will also be called when the server is being reloaded
	 */
	public abstract void onEnable();
	/**
	 * In this function every plugin should stop itself.
	 * This includes removing events, commands, schedule tasks etc.
	 */
	public abstract void onDisable();
}
