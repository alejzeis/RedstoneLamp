package redstonelamp.event.player;

import redstonelamp.entity.Player;
import redstonelamp.event.HandlerList;

public class PlayerJoinEvent extends PlayerEvent {
	
	private static final HandlerList handlers = new HandlerList();
	
	public PlayerJoinEvent(Player player) {
		super(player);
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
