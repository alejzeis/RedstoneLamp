package net.redstonelamp.cmd.exception;

public class InvalidCommandSenderException extends Exception {
    private static final long serialVersionUID = 1L;
    
    public InvalidCommandSenderException() {
        super("CommandSender must be an instance of Server or Player");
    }
}
