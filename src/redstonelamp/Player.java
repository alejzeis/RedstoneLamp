package redstonelamp;


import redstonelamp.network.packet.DataPacket;

import java.net.InetSocketAddress;

/**
 * Represents a player, playing on the server.
 */
public class Player {
    private Server server;

    private long clientId;
    private String identifier;
    private InetSocketAddress address;

    public Player(Server server, String identifier, String address, int port, long clientId){
        this.server = server;
        this.identifier = identifier;
        this.address = new InetSocketAddress(address, port);
        this.clientId = clientId;
    }

    public void handleDataPacket(DataPacket packet){

    }

    public String getIdentifier() {
        return identifier;
    }
}
