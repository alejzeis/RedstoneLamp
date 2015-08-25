package net.redstonelamp.level;

import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents a Level in a World
 *
 * @author RedstoneLamp Team
 */
public class Level {
    private List<Chunk> loadedChunks = new CopyOnWriteArrayList<>();
    private LevelProvider provider;

    public Level(File levelDir) {

    }
}
