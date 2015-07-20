package redstonelamp.event.player;

import redstonelamp.Player;
import redstonelamp.RedstoneLamp;
import redstonelamp.event.Cancellable;
import redstonelamp.event.Event;
import redstonelamp.event.Listener;

public class PlayerJoinEvent extends PlayerEvent implements Cancellable {
	private String type = "PlayerJoinEvent";
	private Player player;
	private Event e = this;
	
	public PlayerJoinEvent(Player player) {
		this.player = player;
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
}
