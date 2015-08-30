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
package net.redstonelamp.network.pc.codec;

import net.redstonelamp.network.UniversalPacket;

public class HandshakePacket{

    public int protocol;
    public String address;
    public int port;
    public int nextState;

    public final int STATUS = 1;
    public final int LOGIN = 2;

    public HandshakePacket(UniversalPacket packet){
        this.protocol = packet.bb().getVarInt();
        this.address = packet.bb().getVarString();
        this.port = packet.bb().getUnsignedShort();
        this.nextState = packet.bb().getVarInt();
    }

}
