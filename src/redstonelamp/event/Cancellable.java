package redstonelamp.event;

public interface Cancellable {
	
	public boolean isCancelled();
	
	public void setCancelled(boolean isCancelled);
	
}
