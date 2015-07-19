package redstonelamp.event.player;

import redstonelamp.Player;
import redstonelamp.event.Cancellable;
import redstonelamp.event.Event;
import redstonelamp.event.Listener;

public class PlayerJoinEvent implements Event, Cancellable {
	private String type = "PlayerJoinEvent";
	private Player player;
	
	public PlayerJoinEvent(Player player) {
		this.player = player;
	}
	
	public void execute(Listener listener) {
		listener.onEvent(this);
	}
	
	public void cancel() {
		//TODO: Cancel the event
	}
	
	public String getName() {
		return type;
	}
	
	public Player getPlayer() {
		return player;
	}
}
