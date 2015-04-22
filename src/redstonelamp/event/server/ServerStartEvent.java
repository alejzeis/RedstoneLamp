package redstonelamp.event.server;

import redstonelamp.entity.Player;
import redstonelamp.event.HandlerList;

public class ServerStartEvent extends ServerEvent {
	
	public ServerStartEvent(Player player) {
		super();
	}
	
	@Override
	public HandlerList getHandlers() {
		return null;
	}
	
}
