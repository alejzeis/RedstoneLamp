package net.redstonelamp.level;

/**
 * A Position of a chunk, two integers x and z
 *
 * @author RedstoneLamp Team
 */
public class ChunkPosition {
    private int x;
    private int z;

    /**
     * Create a new ChunkPosition with the specified coordinates x and z
     * @param x The X coordinate of the chunk
     * @param z The Z coordinate of the chunk
     */
    public ChunkPosition(int x, int z) {
        this.x = x;
        this.z = z;
    }

    /**
     * Get the X coordinate of this ChunkPosition
     * @return The X coordinate of the ChunkPosition
     */
    public int getX() {
        return x;
    }

    /**
     * Set the X coordinate of this ChunkPosition
     * @param x The X coordinate of the ChunkPosition
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Get the Z coordinate of this ChunkPosition
     * @return The Z coordinate of the ChunkPosition
     */
    public int getZ() {
        return z;
    }

    /**
     * Set the Z coordinate of this ChunkPosition
     * @param z The Z coordinate of the ChunkPosition
     */
    public void setZ(int z) {
        this.z = z;
    }
}
