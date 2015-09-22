package net.redstonelamp.event.chunk;

import lombok.Getter;
import net.redstonelamp.Player;
import net.redstonelamp.event.Event;

public class ChunkRequestEvent extends Event {
    @Getter private Player player;
    
    public ChunkRequestEvent(Player player) {
        this.player = player;
    }
}
