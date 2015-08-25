package net.redstonelamp.level.provider;

import net.redstonelamp.level.Chunk;
import net.redstonelamp.level.ChunkPosition;
import net.redstonelamp.level.LevelProvider;

import java.io.File;
import java.io.IOException;

/**
 * A fake level
 *
 * @author RedstoneLamp Team
 */
public class FakeLevelProvider implements LevelProvider{

    @Override
    public Chunk getChunk(ChunkPosition position) {
        return null;
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void loadLevelData(File file) throws IOException {

    }
}
