package redstonelamp.event.player;

import redstonelamp.entity.Player;
import redstonelamp.event.HandlerList;

public class PlayerInteractEvent extends PlayerEvent {
	
	public PlayerInteractEvent(Player player) {
		super(player);
	}
	
	@Override
	public HandlerList getHandlers() {
		return null;
	}
	
}
