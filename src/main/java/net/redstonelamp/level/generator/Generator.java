package net.redstonelamp.level.generator;

import net.redstonelamp.level.Chunk;
import net.redstonelamp.level.ChunkPosition;

/**
 * Represents a Level Generator.
 *
 * @author RedstoneLamp Team
 */
public interface Generator {
    /**
     * Generate a new 16 * 16 * 128 Chunk at the specified <code>Position</code>
     * @param position The Chunk's position.
     * @return The newly generated Chunk.
     */
    Chunk generateChunk(ChunkPosition position);
}
