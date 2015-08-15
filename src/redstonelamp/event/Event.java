package redstonelamp.event;

import redstonelamp.Player;
import redstonelamp.RedstoneLamp;
import redstonelamp.Server;
import redstonelamp.event.player.PlayerEvent;
import redstonelamp.event.player.PlayerKickEvent;
import redstonelamp.event.server.ServerEvent;

public abstract class Event {

	public abstract String getEventName();
	
	public void cancel() {
		if(!(this instanceof Cancellable))
			return;
	}
	
	public Player getPlayer() {
		if(!(this instanceof PlayerEvent))
			return null;
		return null;
	}
	
	public String getReason() {
		if(!(this instanceof PlayerEvent) && !(this instanceof PlayerKickEvent))
			return null;
		return null;
	}
	
	public Server getServer() {
		if(!(this instanceof ServerEvent))
			return RedstoneLamp.getServerInstance();
		return null;
	}
}
