package redstonelamp.plugin;

import redstonelamp.event.Event;
import redstonelamp.event.EventException;
import redstonelamp.event.EventPriority;
import redstonelamp.event.Listener;

public class RegisteredListener {
	
	private EventPriority priority;
	private Listener listener;
	private Plugin plugin;
	private EventExecutor executor;
	private boolean ignoreCancelled;
	
	public EventPriority getPriority() {
		return priority;
	}
	
	public RegisteredListener(Listener listener, EventExecutor executor, EventPriority priority, final Plugin plugin, boolean ignoreCancelled) {
		this.listener = listener;
		this.priority = priority;
		this.plugin = plugin;
		this.executor = executor;
		this.ignoreCancelled = ignoreCancelled;
	}
	
	/**
	 * Calls the event executor
	 * 
	 * @param event
	 *            The event
	 * @throws EventException
	 *             If an event handler throws an exception.
	 */
	public void callEvent(final Event event) throws EventException {
		executor.execute(listener, event);
	}
	
	public Plugin getPlugin() {
		return plugin;
	}
	
	public Listener getListener() {
		return listener;
	}
}
