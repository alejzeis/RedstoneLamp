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
 * Different flags for UpdateBlockPacket (protocol 27)
 * <br>
 * From https://github.com/PocketMine/PocketMine-MP/blob/master/src/pocketmine/network/protocol/UpdateBlockPacket.php
 *
 * @author RedstoneLamp Team and PocketMine Team
 */
public final class UpdateBlockPacketFlagsV27{
    public static final byte FLAG_NONE = 0b0000;
    public static final byte FLAG_NEIGHBORS = 0b0001;
    public static final byte FLAG_NETWORK = 0b0010;
    public static final byte FLAG_NOGRAPHIC = 0b0100;
    public static final byte FLAG_PRIORITY = 0b1000;
    public static final byte FLAG_ALL = (FLAG_NEIGHBORS | FLAG_NETWORK);
    public static final byte FLAG_ALL_PRIORITY = (FLAG_ALL | FLAG_PRIORITY);
}
