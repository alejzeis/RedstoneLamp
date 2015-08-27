package net.redstonelamp.event.java;

import lombok.Getter;
import net.redstonelamp.event.EventContext;

public class JavaEvent {
	/**
	 * This event's context
	 */
	@Getter private EventContext eventContext = EventContext.CROSS_PLATFORM;
}
