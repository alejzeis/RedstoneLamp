package redstonelamp.event.server;

import redstonelamp.cmd.CommandSender;
import redstonelamp.event.HandlerList;

public class ServerCommandEvent extends ServerEvent {

	private static final HandlerList handlers = new HandlerList();
    
	private CommandSender sender;
	
	private String line;
	
	public ServerCommandEvent(CommandSender sender, String line) {
		this.sender = sender;
		this.line   = line;
	}
	
	public CommandSender getSender() {
		return sender;
	}
	
	public String getCommand() {
		return line;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

}
