package net.redstonelamp.cmd;

import lombok.Getter;
import net.redstonelamp.Player;
import net.redstonelamp.Server;

public class CommandSender {
    @Getter private Object sender;
    
    public CommandSender(Object sender) {
        this.sender = sender;
    }
    
    public void sendMessage(String message) {
        if(sender instanceof Player)
            ((Player) sender).sendMessage(message);
        else if(sender instanceof Server)
            ((Server) sender).getLogger().info(message);
    }
}
