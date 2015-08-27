package net.redstonelamp.plugin;

import java.util.ArrayList;

import lombok.Getter;
import net.redstonelamp.plugin.exception.AsyncAPICallException;

public abstract class PluginManager {
	/**
	 * Main thread id - to catch async access by plugins and block it
	 */
	@Getter private static long THREAD_ID = -1;
	/**
	 * ArrayList of plugins/pluginloaders managed by this manager
	 */
	@Getter private ArrayList<PluginLoader> pluginLoaders = new ArrayList<>();
	
	/**
	 * Sets some basic values. This method has to be called from the main thread!
	 */
	public static void init(){
		//Store the main thread id
		THREAD_ID = Thread.currentThread().getId();
	}
	
	/**
	 * Loads all plugins managed by this plugin manager
	 */
	public abstract void loadPlugins();
	/**
	 * Checks if a task is running async. Throws an AsyncAPICallException when the task is async.
	 * @param msg The message to use in the AsyncAPICallException
	 * @throws AsyncAPICallException
	 */
	public static void checkAsync(String msg) throws AsyncAPICallException{
		if(Thread.currentThread().getId()!=THREAD_ID){
			throw new AsyncAPICallException(msg);
		}
	}
}
