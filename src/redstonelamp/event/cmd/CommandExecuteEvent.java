package redstonelamp.event.cmd;

import redstonelamp.RedstoneLamp;
import redstonelamp.event.Event;
import redstonelamp.event.Listener;

public class CommandExecuteEvent extends Event {
	private String type = "CommandExecuteEvent";
	private Event e = this;
	
	public void execute(Listener listener) {
		RedstoneLamp.getAsync().execute(() -> listener.onEvent(e));
	}
	
	public String getEventName() {
		return type;
	}
}
