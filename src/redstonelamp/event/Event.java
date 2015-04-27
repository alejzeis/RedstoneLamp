package redstonelamp.event;

public abstract class Event {
	protected String eventName = null;
	private boolean isCancelled = false;
	
	public Event() {
		this(false);
	}
	
	public Event(boolean async) {
		
	}
	
	/**
	 * Returns the events name
	 * 
	 * @return String
	 */
	final public String getEventName() {
		return this.eventName == null ? this.getClass().getSimpleName():this.eventName;
	}
	
	/**
	 * Returns the HandlerList class
	 * 
	 * @return HandlerList
	 */
	public abstract HandlerList getHandlers();
	
	/**
	 * Cancels the event
	 * 
	 * @return boolean
	 */
	public boolean isCancelled() {
		if(!(this instanceof Cancellable)) { throw new IllegalArgumentException("Event is not Cancellable"); }
		return this.isCancelled == true;
	}
	
	/**
	 * Sets the events cancel status
	 * 
	 * @param boolean value
	 */
	public void setCancelled(boolean value) {
		if(!(this instanceof Cancellable)) { throw new IllegalArgumentException("Event is not Cancellable"); }
		this.isCancelled = value;
	}
}
