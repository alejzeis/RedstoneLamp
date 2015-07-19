package redstonelamp.event.player;

import redstonelamp.Player;
import redstonelamp.event.Event;
import redstonelamp.event.Listener;

public class PlayerQuitEvent implements Event {
	private String type = "PlayerQuitEvent";
	private Player player;
	
	public PlayerQuitEvent(Player player) {
		this.player = player;
	}

	public void execute(Listener listener) {
		listener.onEvent(this);
	}
	
	public void cancel() {}
	
	public String getName() {
		return type;
	}
	
	public Player getPlayer() {
		return player;
	}
}
