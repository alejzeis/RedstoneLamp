package net.redstonelamp.cmd;

public interface CommandListener {
    public void onCommand(CommandSender sender, String cmd, String label, String[] params);
}
