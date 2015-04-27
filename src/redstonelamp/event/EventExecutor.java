package redstonelamp.event;

public interface EventExecutor {
	/**
	 * Executes an event
	 * 
	 * @param Listener listener
	 * @param Event event
	 * @throws EventException
	 */
	public void execute(Listener listener, Event event) throws EventException;
}
