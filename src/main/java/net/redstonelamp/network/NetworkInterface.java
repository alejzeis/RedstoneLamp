package net.redstonelamp.network;

/**
 * Represents an interface that can read and write packets
 *
 * @author RedstoneLamp Team
 */
public interface NetworkInterface {
    /**
     * Reads a packet from the underlying socket. This method should NOT block, and if no packet is read return null.
     * @return A Packet if received, null if not.
     */
    UniversalPacket readPacket() throws LowLevelNetworkException;

    /**
     * Sends a packet using the underlying socket. This method should NOT block.
     * @param packet The <code>UniversalPacket</code> to be sent.
     */
    void sendPacket(UniversalPacket packet) throws LowLevelNetworkException;
}
