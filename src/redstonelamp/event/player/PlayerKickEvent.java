package redstonelamp.event.player;

import redstonelamp.Player;
import redstonelamp.event.Cancellable;
import redstonelamp.event.Event;
import redstonelamp.event.Listener;

public class PlayerKickEvent extends PlayerEvent implements Cancellable {
	private String type = "PlayerKickEvent";
	private Player player;
	private String reason;
	private Event e = this;

	private boolean canceled;
	
	public PlayerKickEvent(Player player, String reason) {
		this.player = player;
		this.reason = reason;
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
	
	public String getReason() {
		return reason;
	}

	@Override
	public boolean isCanceled() {
		return canceled;
	}

	@Override
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
}
