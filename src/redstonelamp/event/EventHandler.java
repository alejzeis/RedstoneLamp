package redstonelamp.event;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventHandler {
	/**
	 * Gets the events priority
	 * 
	 * @return EventPriority
	 */
	EventPriority priority() default EventPriority.NORMAL;
	
	/**
	 * If true, the event will never be canceled
	 * 
	 * @return boolean
	 */
	boolean ignoreCancelled() default false;
}
