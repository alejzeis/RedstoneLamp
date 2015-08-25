package net.redstonelamp.level.provider.leveldb;

import net.redstonelamp.level.ChunkPosition;
import net.redstonelamp.nio.BinaryBuffer;

import java.nio.ByteOrder;

/**
 * Represents a Key to a LevelDB database. The MCPE keys are always 9 bytes, two little-endian
 * integers for the x and z coordinates, and then one byte for the KeyType.
 *
 * @author RedstoneLamp Team
 */
public enum Key {
    /**
     * This type contains the 16 * 16 * 128 chunk data for the specified coordinates. The format
     * for the chunk data is: blockIds + blockMeta + skylight + blocklight + heightmap + biomeColors.
     *
     * You can find more information on the wiki page: http://minecraft.gamepedia.com/Pocket_Edition_level_format
     */
    TYPE_TERRAIN_DATA((byte) 0x30),
    /**
     * This type contains Tile Entity Data in NBT.
     *
     * You can find more information on the wiki page: http://minecraft.gamepedia.com/Pocket_Edition_level_format
     */
    TYPE_TILE_ENTITY_DATA((byte) 0x31),
    /**
     * This type contains the Entity Data in NBT.
     *
     * You can find more information on the wiki page: http://minecraft.gamepedia.com/Pocket_Edition_level_format
     */
    TYPE_ENTITY_DATA((byte) 0x32),
    /**
     * This type is unknown, but the value is always one byte.
     *
     * You can find more information on the wiki page: http://minecraft.gamepedia.com/Pocket_Edition_level_format
     */
    TYPE_ONE_BYTE_DATA((byte) 0x76);

    private byte type;

    Key(byte type) {
        this.type = type;
    }

    /**
     * Assemble the full Key into a byte array.
     * The format is (position x, position z, type)
     * @param position The ChunkPosition of which chunk to get the data from
     * @return The fully assembled Key, as a byte array
     */
    public byte[] assembleKey(ChunkPosition position) {
        BinaryBuffer bb = BinaryBuffer.newInstance(9, ByteOrder.LITTLE_ENDIAN);
        bb.putInt(position.getX());
        bb.putInt(position.getZ());
        bb.putByte(type);
        return bb.toArray();
    }

    /**
     * Get the type of Key for this Key instance.
     * @return The Key type.
     */
    public byte getKeyType() {
        return type;
    }
}
