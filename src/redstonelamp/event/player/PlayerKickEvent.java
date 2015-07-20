package redstonelamp.event.player;

import redstonelamp.Player;
import redstonelamp.RedstoneLamp;
import redstonelamp.event.Cancellable;
import redstonelamp.event.Event;
import redstonelamp.event.Listener;

public class PlayerKickEvent extends PlayerEvent implements Cancellable {
	private String type = "PlayerKickEvent";
	private Player player;
	private String reason;
	private Event e = this;
	
	public PlayerKickEvent(Player player, String reason) {
		this.player = player;
		this.reason = reason;
	}
	
	public void execute(Listener listener) {
		RedstoneLamp.getAsync().execute(new Runnable() {
			public void run() {
				listener.onEvent(e);
			}
		});
	}
	
	public String getEventName() {
		return type;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public String getReason() {
		return reason;
	}
}
