package redstonelamp.event;

public abstract class Event {
	
	protected String eventName = null;
	private boolean isCancelled = false;
	
	public Event() {
		this(false);
	}
	
	public Event(boolean async) {
		
	}
	
	final public String getEventName() {
		return this.eventName == null ? this.getClass().getSimpleName():this.eventName;
	}
	
	public abstract HandlerList getHandlers();
	
	public boolean isCancelled() {
		if(!(this instanceof Cancellable)) { throw new IllegalArgumentException("Event is not Cancellable"); }
		return this.isCancelled == true;
	}
	
	public void setCancelled(boolean value) {
		if(!(this instanceof Cancellable)) { throw new IllegalArgumentException("Event is not Cancellable"); }
		this.isCancelled = value;
	}
}
