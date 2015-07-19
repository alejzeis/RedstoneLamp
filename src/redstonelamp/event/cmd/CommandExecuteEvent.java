package redstonelamp.event.cmd;

import redstonelamp.Player;
import redstonelamp.event.Event;
import redstonelamp.event.Listener;

public class CommandExecuteEvent implements Event {
	private String type = "CommandExecuteEvent";
	
	public void execute(Listener listener) {
		listener.onEvent(this);
	}
	
	public String getName() {
		return type;
	}
	
	public void cancel() {
		//TODO: Cancel event
	}
	
	public Player getPlayer() {
		return null;
	}
}
