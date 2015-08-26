/*
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


import net.redstonelamp.nio.BinaryBuffer;

import java.net.SocketAddress;
import java.nio.ByteOrder;

/**
 * A universal packet, not binded to ANY protocol.
 *
 * @author RedstoneLamp Team
 */
public class UniversalPacket{
    private byte[] buffer;
    private SocketAddress address;
    private BinaryBuffer bb;

    /**
     * Create a new UniversalPacket with the specified <code>buffer</code> and belonging to the <code>address</code>.
     * The ByteOrder of the underlying DynamicByteBuffer will be Big Endian.
     *
     * @param buffer  The buffer this packet contains.
     * @param address The address this packet came/was sent from.
     */
    public UniversalPacket(byte[] buffer, SocketAddress address){
        this.buffer = buffer;
        this.address = address;
        bb = BinaryBuffer.wrapBytes(buffer, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Create a new UniversalPacket with the specified <code>buffer</code> in <code>order</code> and belonging to the <code>address</code>
     *
     * @param buffer  The buffer this packet contains.
     * @param order   The ByteOrder of the above byte array. This can be Little Endian or Big Endian and is used in creating
     *                the underlying DynamicByteBuffer.
     * @param address The address this packet came/was sent from.
     */
    public UniversalPacket(byte[] buffer, ByteOrder order, SocketAddress address){
        this.buffer = buffer;
        this.address = address;
        bb = BinaryBuffer.wrapBytes(buffer, order);
    }

    /**
     * Returns the DynamicByteBuffer of the packet <code>buffer</code>. This DOES NOT create a new instance of the buffer.
     *
     * @return The DynamicByteBuffer belonging to the packet.
     */
    public BinaryBuffer bb(){
        return bb;
    }

    /**
     * Get the address this packet came/was sent from.
     *
     * @return The packet's original address.
     */
    public SocketAddress getAddress(){
        return address;
    }

    /**
     * Get the buffer of this packet.
     *
     * @return The buffer, as a byte array.
     */
    public byte[] getBuffer(){
        return buffer;
    }
}
