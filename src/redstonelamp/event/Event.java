package redstonelamp.event;

public abstract interface Event {
	public void execute(Listener listener);
	
	public void cancel();
	
	public String getName();
}
