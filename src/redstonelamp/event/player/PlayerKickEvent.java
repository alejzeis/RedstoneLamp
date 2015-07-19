package redstonelamp.event.player;

import redstonelamp.Player;
import redstonelamp.event.Event;
import redstonelamp.event.Listener;

public class PlayerKickEvent implements Event {
	private String type = "PlayerKickEvent";
	private Player player;
	private String reason;
	
	public PlayerKickEvent(Player player, String reason) {
		this.player = player;
		this.reason = reason;
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
	
	public String getReason() {
		return reason;
	}
}
