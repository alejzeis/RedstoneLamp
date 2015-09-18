package net.redstonelamp.cmd;

import javax.script.Invocable;
import javax.script.ScriptException;

import net.redstonelamp.Player;
import net.redstonelamp.RedstoneLamp;
import net.redstonelamp.Server;
import net.redstonelamp.cmd.exception.InvalidCommandSenderException;

public class CommandExecutor {
    public void execute(String cmd, Object sender) throws InvalidCommandSenderException {
        Server server = RedstoneLamp.SERVER;
        if(sender instanceof Server)
            sender = (Server) sender;
        else if(sender instanceof Player)
            sender = (Player) sender;
        else
            throw new InvalidCommandSenderException();
        
        if(cmd.startsWith("/"))
            cmd = cmd.substring(1);
        boolean executed = false;
        String[] params = cmd.split(" ");
        CommandSender commandSender = new CommandSender(sender);
        String label = null; //TODO
        //TODO: Command Execution event
        for(CommandListener l : server.getCommandManager().getListeners()) {
            l.onCommand(commandSender, cmd, label, params);
        }
        for(Invocable i : server.getScriptManager().getScripts()) {
            try {
                i.invokeFunction("onCommand", commandSender, cmd, label, params);
            } catch (ScriptException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {}
        }
        
        if(!executed)
            commandSender.sendMessage("Unknown command! For help, use \"/help\"");
    }
}
