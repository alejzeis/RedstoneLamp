/**
 * This file is part of RedstoneLamp.
 *
 * RedstoneLamp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedstoneLamp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RedstoneLamp.  If not, see <http://www.gnu.org/licenses/>.
 */
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
