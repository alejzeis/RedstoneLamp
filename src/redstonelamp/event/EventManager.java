package redstonelamp.event;

import java.util.ArrayList;

public class EventManager {
	private EventExecutor eventExecutor = new EventExecutor();
	private ArrayList<Object> listeners = new ArrayList<Object>();
	
	/**
	 * Returns all classes events are registered to
	 * 
	 * @return ArrayList<Object>
	 */
	public ArrayList<Object> getListeners() {
		return listeners;
	}
	
	/**
	 * Registers an event listener
	 * 
	 * @param Listener
	 */
	public void registerEventListener(Listener listener) {
		getListeners().add(listener);
	}
	
	/**
	 * Returns the event executor
	 * 
	 * @return EventExecutor
	 */
	public EventExecutor getEventExecutor() {
		return eventExecutor;
	}
}
