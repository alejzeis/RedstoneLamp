package redstonelamp.plugin;

import redstonelamp.event.Event;
import redstonelamp.event.EventException;
import redstonelamp.event.Listener;

public interface EventExecutor {
	/*
	 * @param Listener listener
	 * @param Event event
	 */
	public void execute(Listener listener, Event event) throws EventException; 
}
