package net.redstonelamp.event;

public interface Cancellable {
    /**
     * Cancels the event
     */
    void cancel();
    
    /**
     * Returns true if the event is cancelled
     * 
     * @return
     */
    boolean isCancelled();
}
