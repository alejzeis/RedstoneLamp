package net.redstonelamp.level;

import java.io.File;
import java.io.IOException;

/**
 * Represents a level I/O storage provider.
 */
public interface LevelProvider {
    /**
     * Get a Chunk from a ChunkPosition
     * @param position The Position of the chunk
     * @return The chunk from the disk
     */
    Chunk getChunk(ChunkPosition position);

    /**
     * Called whenever the parent Level class is shutting down. Save the world here.
     */
    void shutdown();

    /**
     * Load the Level data such as the spawn position and level time
     * from the disk.
     * @param file The File that contains the level data
     * @throws IOException If there is an error while loading the data.
     */
    void loadLevelData(File file) throws IOException;
}
