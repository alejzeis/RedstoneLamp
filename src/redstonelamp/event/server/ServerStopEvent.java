package redstonelamp.event.server;

import redstonelamp.RedstoneLamp;
import redstonelamp.Server;
import redstonelamp.event.Event;
import redstonelamp.event.Listener;

public class ServerStopEvent extends Event {
	private Event e = this;
	
	public void execute(Listener listener) {
		listener.onEvent(e);
	}
	
	public Server getServer() {
		return RedstoneLamp.getServerInstance();
	}
}
