package redstonelamp.event.player;

import redstonelamp.Player;
import redstonelamp.event.Cancellable;
import redstonelamp.event.Event;
import redstonelamp.event.Listener;

public class PlayerKickEvent extends Event implements Cancellable {
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
	
	public Player getPlayer() {
		return this.player;
	}
	
	public String getReason() {
		return this.reason;
	}

	@Override
	public boolean isCanceled() {
		return this.canceled;
	}

	@Override
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
}
