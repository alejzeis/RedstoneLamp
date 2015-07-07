package redstonelamp.network;

import redstonelamp.Player;
import redstonelamp.network.packet.DataPacket;

/**
 * Interface for communicating with different handlers, ex: PC and PE handlers
 */
public interface NetworkInterface {
    void sendPacket(Player player, DataPacket packet, boolean needACK, boolean immediate);
    void close(Player player, String reason);
    void setName(String name);

    void processData();
    void shutdown();
    void emergencyShutdown();
}
