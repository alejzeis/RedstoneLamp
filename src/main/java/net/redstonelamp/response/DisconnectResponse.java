package net.redstonelamp.response;

/**
 * Represents a protocol-independent DisconnectionResponse
 *
 * @author RedstoneLamp Team
 */
public class DisconnectResponse extends Response{
    public static final boolean DEFAULT_notifyClient = true;

    public String reason;
    public boolean notifyClient = DEFAULT_notifyClient;

    public DisconnectResponse(String reason) {
        this.reason = reason;
    }
}
