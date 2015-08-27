package net.redstonelamp.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to use in listeners to register a method as event handler
 */
@Target(value=ElementType.METHOD)
@Retention(value=RetentionPolicy.RUNTIME)
public @interface EventSystem {
	public EventPriority priority() default EventPriority.NORMAL;
	public boolean ignoreCancelled() default false;
	public EventContext eventContext() default EventContext.CROSS_PLATFORM;
}
