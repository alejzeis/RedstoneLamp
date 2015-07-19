package redstonelamp.event.server;

import redstonelamp.Player;
import redstonelamp.event.Event;
import redstonelamp.event.Listener;

public class ServerTickEvent implements Event {
	private String type = "ServerTickEvent";
	
	public void execute(Listener listener) {
		listener.onEvent(this);
	}
	
	public void cancel() {}
	
	public String getName() {
		return type;
	}
	
	public Player getPlayer() {
		return null;
	}
}
