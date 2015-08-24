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
package net.redstonelamp.nio;


import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * An NIO buffer class to wrap around a java.nio.ByteBuffer.
 * <br>
 * This buffer is dynamic, as it changes size when the allocated amount is too small.
 *
 * @author RedstoneLamp Team
 */
public class DynamicByteBuffer {
    private final ByteBuffer bb;

    protected DynamicByteBuffer(ByteBuffer bb) {
        this.bb = bb;
    }

    /**
     * Create a new DynamicByteBuffer wrapped around a byte array with the specified <code>order</code>
     * @param bytes The byte array to be wrapped around
     * @param order The ByteOrder of the buffer, Big Endian or Little Endian.
     * @return A new DynamicByteBuffer class, at position zero wrapped around the byte array in the specified <code>order</code>
     */
    public static DynamicByteBuffer wrapBytes(byte[] bytes, ByteOrder order) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.order(order);
        bb.position(0);
        return new DynamicByteBuffer(bb);
    }

    /**
     * Create a new DynamicByteBuffer with the specified <code>initalSize</code> and <code>order</code>
     * <br>
     * The Buffer will grow if an attempt is to put more data than the <code>initalSize</code>
     * @param initalSize The inital size of the buffer
     * @param order The ByteOrder of the buffer, Big Endian or Little Endian
     * @return A new DynamicByteBuffer class, at position zero with the specified <code>order</code> and <code>initalSize</code>
     */
    public static DynamicByteBuffer newInstance(int initalSize, ByteOrder order) {
        ByteBuffer bb = ByteBuffer.allocate(initalSize);
        bb.order(order);
        bb.position(0);
        return new DynamicByteBuffer(bb);
    }

    /**
     * Get a single line string containing each byte of the buffer in hexadecimal
     * @return A String containing each byte of the buffer in hexadecimal with no newlines.
     */
    public String singleLineHexDump() {
        StringBuilder sb = new StringBuilder();
        byte[] data = bb.array();
        for(byte b : data) {
            sb.append(String.format("%02X", b) + " ");
        }
        return sb.toString();
    }
}
