package redstonelamp.event.player;

import redstonelamp.entity.Player;
import redstonelamp.event.HandlerList;

public class PlayerDisconnectEvent extends PlayerEvent {
	
	public PlayerDisconnectEvent(Player player) {
		super(player);
	}
	
	@Override
	public HandlerList getHandlers() {
		return null;
	}
	
}
