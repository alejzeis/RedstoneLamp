package redstonelamp.cmd;

import redstonelamp.Player;
import redstonelamp.RedstoneLamp;
import redstonelamp.Server;
import redstonelamp.utils.TextFormat;

public class CommandSender {
	private Object sender;
	
	public CommandSender(Object sender) {
		this.sender = sender;
	}
	
	/**
	 * Sends a message to the sender
	 * 
	 * @param String
	 */
	public void sendMessage(String message) {
		if(sender instanceof Player)
			((Player) sender).sendMessage(message);
		if(sender instanceof Server)
			RedstoneLamp.getServerInstance().getLogger().info(TextFormat.stripColors(message));
	}
	
	/**
	 * Returns the senders Object
	 * 
	 * @return Server|Player
	 */
	public Object getSender() {
		return sender;
	}
	
	/**
	 * Denies sender due to permissions
	 */
	public void noPermission() {
		sendMessage("You do not have permission to do that!");
	}
	
	/**
	 * Checks if sender is an operator
	 * 
	 * @return boolean
	 */
	public boolean isOp() {
		if(sender instanceof Player)
			return ((Player) sender).isOp();
		if(sender instanceof Server)
			return true;
		return false;
	}
}
