/*
 * This file is part of RedstoneLamp.
 *
 * RedstoneLamp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedstoneLamp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RedstoneLamp.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.redstonelamp.plugin.java;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

import net.redstonelamp.event.Cancellable;
import net.redstonelamp.event.Event;
import net.redstonelamp.event.EventHandler;
import net.redstonelamp.event.EventPlatform;
import net.redstonelamp.event.EventPriority;
import net.redstonelamp.event.Listener;
import net.redstonelamp.plugin.Plugin;
import net.redstonelamp.plugin.PluginManager;
import net.redstonelamp.plugin.exception.PluginException;

public class JavaPluginManager extends PluginManager {
	
	private final JavaPluginLoader loader;
	private final HashMap<String, JavaPlugin> plugins;
	private final HashMap<Listener, JavaPlugin> listeners;

	public JavaPluginManager(JavaPluginLoader loader) throws IOException {
		this.loader = loader;
		this.plugins = new HashMap<String, JavaPlugin>();
		this.listeners = new HashMap<Listener, JavaPlugin>();
	}

	@Override
	public String getFileType() {
		return ".jar";
	}

	@Override
	public void loadPlugins() throws PluginException, IOException {
		for (Plugin plugin : loader.loadPlugins().values())
			plugins.put(plugin.getName(), (JavaPlugin) plugin);
	}

	@Override
	public void unloadPlugins() {
		loader.unloadPlugins();
		plugins.clear();
	}
	
	@Override
	public Plugin[] getPlugins() {
		return plugins.values().toArray(new Plugin[plugins.size()]);
	}

	public void registerEvents(Listener listener, JavaPlugin plugin) {
		listeners.put(listener, plugin);
	}

	public void unregisterEvents(Listener listener, JavaPlugin plugin) {
		listeners.remove(listener, plugin);
	}

	public Listener[] getListeners() {
		return listeners.values().toArray(new Listener[listeners.size()]);
	}
	
	@Override
	public void callEvent(EventPlatform platform, Event event) {
		HashMap<Method, Listener> handlers = new HashMap<Method, Listener>();
		
		// Add methods and their instances
		for(Listener listener : getListeners()) {
			for(Method method : listener.getClass().getDeclaredMethods()) {
				// Make sure the method has the EventHandler annotation and the method has only one parameter
				if(method.isAnnotationPresent(EventHandler.class) && method.getParameters().length == 1) {
					if(method.getParameterTypes()[0].equals(event.getClass())) {
						EventHandler data = (EventHandler) method.getAnnotation(EventHandler.class);
						if(platform.equals(data.platform()) || data.platform().equals(EventPlatform.BOTH)) {
							handlers.put(method, listener);
						}
					}
				}
			}
		}
		
		// Now invoke them in the correct order
		for(EventPriority priority : EventPriority.values()) {
			for(Method method : handlers.keySet()) {
				EventHandler data = (EventHandler) method.getAnnotation(EventHandler.class);
				if(data.priority().equals(priority)) {
					boolean cancelled = false;
					if(event instanceof Cancellable)
						cancelled = (((Cancellable) event).isCancelled() && data.ignoreCancelled() == false);
					if(!cancelled) {
						try {
							method.invoke(handlers.get(method), event);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}
	}

}
