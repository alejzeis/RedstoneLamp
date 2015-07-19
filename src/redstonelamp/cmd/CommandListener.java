package redstonelamp.cmd;

public interface CommandListener {
	public void onCommand(CommandSender sender, Command cmd, String label, String[] args);
}
