package net.redstonelamp.event.block;

import net.redstonelamp.event.Cancellable;
import net.redstonelamp.event.Event;

public class BlockBreakEvent extends Event implements Cancellable {
    private boolean cancelled = false;
    
    @Override
    public void cancel() {
        cancelled = true;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
