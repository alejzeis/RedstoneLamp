package redstonelamp.cmd;

import redstonelamp.Player;
import redstonelamp.RedstoneLamp;

public class CommandSender {
	private Object sender;
	
	public CommandSender(String sender) {
		this.sender = sender;
	}
	
	public void sendMessage(String message) {
		if(sender instanceof Player)
			((Player) sender).sendMessage(message);
		if(this.sender.equals("Console"))
			RedstoneLamp.getServerInstance().getLogger().info(message);
	}
	
	public void noPermission() {
		sendMessage("You do not have permission to do that!");
	}

	public boolean isOp() {
		if(sender instanceof Player)
			return ((Player) sender).isOp();
		if(sender.equals("Console"))
			return true;
		return false;
	}
}
