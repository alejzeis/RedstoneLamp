package redstonelamp.event.player;

import redstonelamp.Player;
import redstonelamp.event.Event;
import redstonelamp.event.Listener;

public class PlayerQuitEvent extends Event {
	private Player player;
	private Event e = this;
	
	public PlayerQuitEvent(Player player) {
		this.player = player;
	}
	
	public void execute(Listener listener) {
		listener.onEvent(e);
	}
	
	public Player getPlayer() {
		return this.player;
	}
}
