package net.redstonelamp.utils;

import org.spout.nbt.CompoundTag;
import org.spout.nbt.stream.NBTOutputStream;

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
}
