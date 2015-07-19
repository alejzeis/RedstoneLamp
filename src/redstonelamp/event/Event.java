package redstonelamp.event;

public abstract interface Event {
	public void execute(Listener listener);
	
	public String getName();
}
