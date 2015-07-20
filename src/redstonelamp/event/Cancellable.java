package redstonelamp.event;

public interface Cancellable {
	boolean isCanceled();
    void setCanceled(boolean canceled);
}
