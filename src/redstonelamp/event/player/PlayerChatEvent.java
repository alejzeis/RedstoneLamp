package redstonelamp.event.player;

import redstonelamp.Player;
import redstonelamp.event.Cancellable;
import redstonelamp.event.Event;
import redstonelamp.event.Listener;

public class PlayerChatEvent extends PlayerEvent implements Cancellable {
	private String type = "PlayerChatEvent";
	private Player player;
	private String message;
	private Event e = this;
	
	private boolean canceled;
	
	public PlayerChatEvent(Player player, String message) {
		this.player = player;
		this.message = message;
	}

	public void execute(Listener listener) {
		listener.onEvent(e);
	}
	
	public String getEventName() {
		return type;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public String getMessage() {
		return message;
	}
	
	public boolean isCanceled() {
		return canceled;
	}
	
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
}
