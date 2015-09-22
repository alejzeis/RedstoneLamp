package net.redstonelamp.event.player;

import lombok.Getter;
import net.redstonelamp.Player;
import net.redstonelamp.event.Cancellable;
import net.redstonelamp.event.Event;

public class PlayerChatEvent extends Event implements Cancellable {
    @Getter private Player player;
    @Getter private String message;
    private boolean cancelled = false;
    
    public PlayerChatEvent(Player player, String message) {
        this.player = player;
        this.message = message;
    }
    
    @Override
    public void cancel() {
        cancelled = true;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
