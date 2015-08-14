package redstonelamp.event.server;

import redstonelamp.RedstoneLamp;
import redstonelamp.Server;
import redstonelamp.event.Event;
import redstonelamp.event.Listener;

public class ServerTickEvent extends ServerEvent {
	private String type = "ServerTickEvent";
	private Event e = this;
	
	public void execute(Listener listener) {
		listener.onEvent(e);
	}
	
	public String getEventName() {
		return type;
	}
	
	public Server getServer() {
		return RedstoneLamp.getServerInstance();
	}
}
