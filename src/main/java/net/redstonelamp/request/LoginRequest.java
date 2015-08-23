package net.redstonelamp.request;

/**
 * Represents a Login Request
 *
 * @author RedstoneLamp Team
 */
public class LoginRequest extends Request{
    public String username;
    public long clientId;
    public long authid;

    public LoginRequest(String username){
        this.username = username;
    }

    @Override
    public void execute() {
        //TODO?
    }
}
