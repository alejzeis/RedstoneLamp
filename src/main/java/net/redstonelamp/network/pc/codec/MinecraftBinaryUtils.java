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

import net.redstonelamp.level.position.Position;
import net.redstonelamp.nio.BinaryBuffer;

/**
 * Binary utility class for the MCPC protocol.
 *
 * @author RedstoneLamp Team
 */
public class MinecraftBinaryUtils {

    public static void writePosition(Position pos, BinaryBuffer bb) {
        bb.putLong(((long) pos.getX() & 0x3FFFFFF) << 38 | ((long) pos.getY() & 0xFFF) << 26 | (long) pos.getZ() & 0x3FFFFFF);
    }

    public static Position readPosition(BinaryBuffer bb) {
        long val = bb.getLong();
        Position pos = new Position(null);
        pos.setX(val >> 38);
        pos.setY(val >> 26 & 0xFFF);
        pos.setZ(val << 38 >> 38);
        return pos;
    }
}
