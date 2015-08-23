package net.redstonelamp;

import net.redstonelamp.network.Protocol;
import net.redstonelamp.request.Request;

/**
 * Protocol-independent Player class. Represents a Player on the server
 *
 * @author RedstoneLamp Team
 */
public class Player {
    private final Protocol protocol;
    private final String identifier;

    /**
     * Construct a new Player instance belonging to the specified <code>Protocol</code> with the <code>identifier</code>
     * @param protocol The protocol this player belongs to
     * @param identifier The client's identifier. This is the address the player is connecting from, in the format:
     *                   [ip]:[port]
     */
    public Player(Protocol protocol, String identifier) {
        this.protocol = protocol;
        this.identifier = identifier;
    }

    public void handleRequest(Request request) {

    }

    public Protocol getProtocol() {
        return protocol;
    }

    public String getIdentifier() {
        return identifier;
    }
}
