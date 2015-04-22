package redstonelamp.cmd;

import redstonelamp.Server;

public interface CommandSender {
	
	/*
	 * @param String message
	 */
	public void sendMessage(String message);
	
	/*
	 * @return Server
	 */
	public Server getServer();
	
	/*
	 * @return String
	 */
	public String getName();
	
}
