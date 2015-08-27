package net.redstonelamp.event.java;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents an event that can be cancelled
 */
public class CancellableJavaEvent extends JavaEvent{
	/**
	 * The events cancel variable
	 */
	@Getter @Setter private boolean cancelled = false;
}
