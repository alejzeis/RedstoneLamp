package redstonelamp.level.generator;

import redstonelamp.level.Chunk;

/**
 * Represents a Level Generator.
 *
 * @author jython234
 */
public interface Generator {
    Chunk generateChunk(int cx, int cz);
}
