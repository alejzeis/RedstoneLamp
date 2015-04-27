package redstonelamp.event;

public interface Cancellable {
	/**
	 * Cancels the event
	 * 
	 * @return boolean
	 */
	public boolean isCancelled();
	
	/**
	 * Sets the events cancel status
	 * 
	 * @param boolean isCancelled
	 */
	public void setCancelled(boolean isCancelled);
}
