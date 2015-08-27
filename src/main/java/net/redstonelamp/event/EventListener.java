package net.redstonelamp.event;

import net.redstonelamp.event.java.JavaEvent;

public abstract interface EventListener {
	/**
	 * Handles receiving of an event, as long as the priority matches
	 */
	public abstract void receiveEvent(JavaEvent e, EventPriority priority);
}
