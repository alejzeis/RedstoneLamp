package net.redstonelamp.network;


import java.net.SocketAddress;

/**
 * A universal packet, not binded to ANY protocol.
 *
 * @author RedstoneLamp Team
 */
public class UniversalPacket {
    private byte[] buffer;
    private SocketAddress address;

    /**
     * Create a new UniversalPacket with the specified <code>buffer</code> and belonging to the <code>address</code>
     * @param buffer The buffer this packet contains.
     * @param address The address this packet came/was sent from.
     */
    public UniversalPacket(byte[] buffer, SocketAddress address) {
        this.buffer = buffer;
        this.address = address;
    }

    /**
     * Get the address this packet came/was sent from.
     * @return The packet's original address.
     */
    public SocketAddress getAddress() {
        return address;
    }

    /**
     * Get the buffer of this packet.
     * @return The buffer, as a byte array.
     */
    public byte[] getBuffer() {
        return buffer;
    }
}
