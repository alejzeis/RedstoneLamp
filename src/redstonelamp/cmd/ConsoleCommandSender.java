package redstonelamp.cmd;

import redstonelamp.RedstoneLamp;
import redstonelamp.Server;

public class ConsoleCommandSender implements CommandSender {
	
	@Override
	public String getName() {
		return "Console";
	}
	
	@Override
	public Server getServer() {
		return RedstoneLamp.server;
	}
	
	@Override
	public void sendMessage(String message) {
		RedstoneLamp.server.getLogger().info(message);
	}
	
}
