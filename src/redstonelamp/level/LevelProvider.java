package redstonelamp.level;

import java.io.File;
import java.io.IOException;

import redstonelamp.level.location.ChunkLocation;

/**
 * Represents a level I/O storage provider.
 */
public interface LevelProvider {
    @Deprecated
    byte[] orderChunk(int x, int z);

    Chunk getChunk(ChunkLocation chunkLocation);
    void shutdown();

    void loadLevelData(File file) throws IOException;
}
