package redstonelamp.cmd;

import redstonelamp.Server;

public interface CommandSender {
	public void sendMessage(String message);
	
	public Server getServer();
	
	public String getName();
}
