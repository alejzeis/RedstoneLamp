package redstonelamp.level;

import java.io.File;
import java.io.IOException;

/**
 * Represents a level I/O storage provider.
 */
public interface LevelProvider {
    byte[] orderChunk(int x, int z);
    void shutdown();

    void loadLevelData(File file) throws IOException;
}
