package redstonelamp.event.player;

import redstonelamp.Player;
import redstonelamp.event.Cancellable;
import redstonelamp.event.Event;
import redstonelamp.event.Listener;
import redstonelamp.level.location.Location;

public class PlayerMoveEvent extends Event implements Cancellable {
	private Player player;
	private Event e = this;
	private Location location;
	private boolean canceled;
	
	public PlayerMoveEvent(Player player, Location location) {
		this.player = player;
		this.location = location;
	}

	public void execute(Listener listener) {
		listener.onEvent(e);
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Location getLocation() {
		return this.location;
	}
	
	public boolean isCanceled() {
		return this.canceled;
	}
	
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
}
