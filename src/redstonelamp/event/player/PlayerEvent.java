package redstonelamp.event.player;

import redstonelamp.Player;
import redstonelamp.event.Event;

public abstract class PlayerEvent extends Event {
	public abstract Player getPlayer();
	
	public String getReason() {
		return null;
	}
	
	public String getMessage() {
		return null;
	}
}
