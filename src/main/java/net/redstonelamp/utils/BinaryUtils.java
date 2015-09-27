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
package net.redstonelamp.utils;

import org.spout.nbt.CompoundTag;
import org.spout.nbt.Tag;
import org.spout.nbt.stream.NBTInputStream;
import org.spout.nbt.stream.NBTOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Various Binary utility methods
 *
 * @author RedstoneLamp Team
 */
public class BinaryUtils {

    public static byte[] writeNBT(CompoundTag tag) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try {
            NBTOutputStream out = new NBTOutputStream(bytes);
            out.writeTag(tag);
            out.close();
            return bytes.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Tag readNBTTag(byte[] bytes) {
        try {
            NBTInputStream in = new NBTInputStream(new ByteArrayInputStream(bytes));
            Tag t = in.readTag();
            in.close();
            return t;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
