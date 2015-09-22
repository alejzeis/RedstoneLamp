package net.redstonelamp.event.player;

import lombok.Getter;
import net.redstonelamp.Player;
import net.redstonelamp.event.Event;

public class PlayerSpawnEvent extends Event {
    @Getter private Player player;
    
    public PlayerSpawnEvent(Player player) {
        this.player = player;
    }
}
