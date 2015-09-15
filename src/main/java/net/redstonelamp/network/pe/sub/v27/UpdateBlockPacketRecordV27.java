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
package net.redstonelamp.network.pe.sub.v27;

/**
 * Represents a record in UpdateBlockPacket (protocol 27)
 *
 * @author RedstoneLamp Team
 */
public class UpdateBlockPacketRecordV27{
    public final int x;
    public final int y;
    public final int z;
    public final byte id;
    public final byte meta;
    public final byte flags;

    public UpdateBlockPacketRecordV27(int x, int y, int z, byte id, byte meta, byte flags){
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = id;
        this.meta = meta;
        this.flags = flags;
    }
}
