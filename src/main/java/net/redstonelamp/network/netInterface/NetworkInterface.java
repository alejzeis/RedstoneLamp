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
package net.redstonelamp.network.netInterface;

import net.redstonelamp.network.LowLevelNetworkException;
import net.redstonelamp.network.UniversalPacket;

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
     * @param immediate If the packet should be sent immediately.
     */
    void sendPacket(UniversalPacket packet, boolean immediate) throws LowLevelNetworkException;
}
