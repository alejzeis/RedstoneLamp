package redstonelamp;

import redstonelamp.network.JRakLibInterface;
import redstonelamp.network.NetworkInfo;
import redstonelamp.network.packet.BatchPacket;
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
    private boolean connected = false;

    private JRakLibInterface rakLibInterface;

    public Player(Server server, JRakLibInterface rakLibInterface, String identifier, String address, int port, long clientId){
        this.server = server;
        this.rakLibInterface = rakLibInterface;
        this.identifier = identifier;
        this.address = new InetSocketAddress(address, port);
        this.clientId = clientId;
        connected = true;
    }

    public void handleDataPacket(DataPacket packet){
        if(!connected){
            return;
        }

        if(packet.getBuffer()[0] == NetworkInfo.BATCH_PACKET){
            server.getNetwork().processBatch((BatchPacket) packet, this);
            return;
        }

        switch (packet.getBuffer()[0]){
            default:
                if(server.isDebugMode()){
                    server.getLogger().debug("Packet ("+packet.getClass().getName()+") "+String.format("%02X ", packet.getBuffer()[0]));
                }
        }
    }

    public String getIdentifier() {
        return identifier;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public boolean isConnected() {
        return connected;
    }

    public void sendDirectDataPacket(DataPacket packet){
        if(!connected){
            return;
        }
        //TODO: Call datapacket send event
        rakLibInterface.sendPacket(this, packet, false, true);
    }

    public void sendDataPacket(DataPacket packet) {
        if(!connected){
            return;
        }
        //TODO: Call datapacket send event
        rakLibInterface.sendPacket(this, packet, false, false);
    }
}
