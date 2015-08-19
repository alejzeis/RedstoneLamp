package redstonelamp.event;

import java.util.ArrayList;

public class EventManager {
	private ArrayList<Listener> listeners = new ArrayList<Listener>();
	
	/**
	 * Returns all classes events are registered to
	 * 
	 * @return ArrayList<Object>
	 */
	public ArrayList<Listener> getListeners() {
		return listeners;
	}
	
	/**
	 * Registers an event listener
	 * 
	 * @param Listener
	 */
	public void registerEvents(Listener listener) {
		getListeners().add(listener);
	}
}
