package redstonelamp.event.player;

import redstonelamp.entity.Location;
import redstonelamp.entity.Player;
import redstonelamp.event.HandlerList;

public class PlayerMoveEvent extends PlayerEvent {
	private static final HandlerList handlers = new HandlerList();
	private boolean cancel = false;
	private Location from;
	private Location to;
	
	public PlayerMoveEvent(final Player player) {
		super(player);
	}
	
	public boolean isCancelled() {
		return cancel;
	}
	
	public void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
	
	/**
	 * Gets the location this player moved from
	 *
	 * @return Location the player moved from
	 */
	public Location getFrom() {
		return from;
	}
	
	public void setFrom(Location from) {
		this.from = from;
	}
	
	public Location getTo() {
		return to;
	}
	
	public void setTo(Location to) {
		this.to = to;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
}
