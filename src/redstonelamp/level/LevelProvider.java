package redstonelamp.level;

/**
 * Represents a level I/O storage provider.
 */
public interface LevelProvider {
    byte[] orderChunk(int x, int z);
    void shutdown();
}
