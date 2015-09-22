package net.redstonelamp.cmd.defaults;

import net.redstonelamp.RedstoneLamp;
import net.redstonelamp.cmd.CommandListener;
import net.redstonelamp.cmd.CommandSender;

public class StopCommand implements CommandListener {
    @Override
    public void onCommand(CommandSender sender, String cmd, String label, String[] params) {
        RedstoneLamp.SERVER.stop();
    }
}
