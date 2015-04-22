package redstonelamp.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import redstonelamp.plugin.Plugin;
import redstonelamp.plugin.RegisteredListener;

public class HandlerList {
	
	private volatile RegisteredListener[] handlers = null;
	private final EnumMap<EventPriority, ArrayList<RegisteredListener>> handlerslots;
	private static ArrayList<HandlerList> allLists = new ArrayList<HandlerList>();
	
	public HandlerList() {
		handlerslots = new EnumMap<EventPriority, ArrayList<RegisteredListener>>(EventPriority.class);
		for(EventPriority o : EventPriority.values()) {
			handlerslots.put(o, new ArrayList<RegisteredListener>());
		}
		synchronized(allLists) {
			allLists.add(this);
		}
	}
	
	public static void bakeAll() {
		synchronized(allLists) {
			for(HandlerList h : allLists) {
				h.bake();
			}
		}
	}
	
	/**
	 * Bake HashMap
	 */
	public synchronized void bake() {
		if(handlers != null)
			return;
		List<RegisteredListener> entries = new ArrayList<RegisteredListener>();
		for(Entry<EventPriority, ArrayList<RegisteredListener>> entry : handlerslots.entrySet()) {
			entries.addAll(entry.getValue());
		}
		handlers = entries.toArray(new RegisteredListener[entries.size()]);
	}
	
	public synchronized void register(RegisteredListener listener) {
		if(handlerslots.get(listener.getPriority()).contains(listener))
			throw new IllegalStateException("This listener is already registered to priority " + listener.getPriority().toString());
		handlers = null;
		handlerslots.get(listener.getPriority()).add(listener);
	}
	
	/*
	 * register all listeners
	 */
	public void registerAll(Collection<RegisteredListener> listeners) {
		for(RegisteredListener listener : listeners) {
			register(listener);
		}
	}
	
	/*
	 * remove a listener
	 */
	public synchronized void unregister(RegisteredListener listener) {
		if(handlerslots.get(listener.getPriority()).remove(listener)) {
			handlers = null;
		}
	}
	
	/**
	 * Remove a specific plugin's listeners from this handler
	 * 
	 * @param plugin
	 *            plug-in to remove
	 */
	public synchronized void unregister(Plugin plugin) {
		boolean changed = false;
		for(List<RegisteredListener> list : handlerslots.values()) {
			for(ListIterator<RegisteredListener> i = list.listIterator(); i.hasNext();) {
				if(i.next().getPlugin().equals(plugin)) {
					i.remove();
					changed = true;
				}
			}
		}
		if(changed)
			handlers = null;
	}
	
	/*
	 * removes a specific listener
	 */
	public synchronized void unregister(Listener listener) {
		boolean changed = false;
		for(List<RegisteredListener> list : handlerslots.values()) {
			for(ListIterator<RegisteredListener> i = list.listIterator(); i.hasNext();) {
				if(i.next().getListener().equals(listener)) {
					i.remove();
					changed = true;
				}
			}
		}
		if(changed)
			handlers = null;
	}
	
	/*
	 * get all registered listeners
	 */
	public RegisteredListener[] getRegisteredListeners() {
		return handlers;
	}
}
