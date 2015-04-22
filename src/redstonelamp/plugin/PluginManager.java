package redstonelamp.plugin;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.Map.Entry;

import redstonelamp.Server;
import redstonelamp.cmd.CommandMap;
import redstonelamp.cmd.SimpleCommandMap;
import redstonelamp.event.Event;
import redstonelamp.event.EventException;
import redstonelamp.event.HandlerList;
import redstonelamp.event.Listener;

public class PluginManager {
	
	private Server server;
	private CommandMap commandMap;
	private ArrayList<Plugin> plugins = new ArrayList<Plugin>();
	private PluginLoader loader;
	
	public PluginManager(Server server, SimpleCommandMap commandMap) {
		this.server = server;
		this.commandMap = commandMap;
	}
	
	/*
	 * Register Plug-in loader class
	 */
	public void registerPluginLoader(PluginLoader loader) {
		this.loader = loader;
	}
	
	/*
	 * Loads all Plug-ins
	 */
	public void loadPlugins(File folder) {
		File[] listOfFiles = folder.listFiles();
		for(File file : listOfFiles) {
			if(file.isFile() && file.getName().toLowerCase().endsWith(".java")) {
				server.getLogger().info(": Plug in -> " + file);
				loader.preparePluginFiles(file);
			}
		}
		plugins = loader.loadJavaPlugins();
	}
	
	/*
	 * this method will be called from plug-ins for registering listeners
	 */
	public void registerEvents(Listener listener, Plugin plugin) {
		if(!plugin.isEnabled())
			throw new IllegalStateException("Plugin attempted to register " + listener + " while not enabled");
		for(Entry<Class<? extends Event>, Set<RegisteredListener>> entry : loader.createRegisteredListeners(listener, plugin).entrySet()) {
			getEventListeners(getRegistrationClass(entry.getKey())).registerAll(entry.getValue());
		}
		HandlerList.bakeAll();
	}
	
	private HandlerList getEventListeners(Class<? extends Event> type) {
		try {
			Method method = getRegistrationClass(type).getDeclaredMethod("getHandlerList");
			method.setAccessible(true);
			return (HandlerList) method.invoke(null);
		} catch(Exception e) {
			throw new IllegalArgumentException(e.toString());
		}
	}
	
	private Class<? extends Event> getRegistrationClass(Class<? extends Event> clazz) {
		try {
			clazz.getDeclaredMethod("getHandlerList");
			return clazz;
		} catch(NoSuchMethodException e) {
			if(clazz.getSuperclass() != null && !clazz.getSuperclass().equals(Event.class) && Event.class.isAssignableFrom(clazz.getSuperclass())) {
				return getRegistrationClass(clazz.getSuperclass().asSubclass(Event.class));
			} else {
				throw new IllegalArgumentException("Unable to find handler list for event " + clazz.getName());
			}
		}
	}
	
	/*
	 * calls specified event ex PlayerMoveEvent
	 */
	public void callEvent(Event event) {
		fireEvent(event);
	}
	
	private void fireEvent(Event event) {
		HandlerList handlers = event.getHandlers();
		RegisteredListener[] listeners = handlers.getRegisteredListeners();
		for(RegisteredListener registration : listeners) {
			if(!registration.getPlugin().isEnabled()) {
				continue;
			}
			try {
				registration.callEvent(event);
			} catch(EventException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/*
	 * return s plug-in
	 */
	public Plugin getPlugin(final String name) {
		for(Plugin p : plugins) {
			if(name.equals(p.getName()))
				return p;
		}
		return null;
	}
	
	/*
	 * Returns all plug-ins
	 */
	public ArrayList<Plugin> getPlugins() {
		return plugins;
	}
	
	/*
	 * disable a currently enabled plug-in
	 */
	public void disablePlugin(Plugin plugin) {
		loader.disablePlugin(plugin);
	}
	
}
