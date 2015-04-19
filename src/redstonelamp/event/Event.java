package redstonelamp.event;

import redstonelamp.Player;

public abstract class Event {

	private Player player;

	public Event(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return null;

	}

	public HandlerList getHandlers() {
		return null;
	}
}
