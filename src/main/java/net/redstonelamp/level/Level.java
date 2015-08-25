package net.redstonelamp.level;

import net.redstonelamp.level.generator.FlatGenerator;
import net.redstonelamp.level.generator.Generator;
import net.redstonelamp.level.position.Position;
import net.redstonelamp.level.provider.LevelLoadException;
import net.redstonelamp.level.provider.LevelProvider;
import net.redstonelamp.level.provider.leveldb.LevelDBProvider;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents a Level in a World
 *
 * @author RedstoneLamp Team
 */
public class Level {
    private final LevelManager manager;
    private List<Chunk> loadedChunks = new CopyOnWriteArrayList<>();
    private LevelProvider provider;

    private String name;
    private Position spawnPosition;
    private int gamemode;
    private int time;
    private Generator generator;

    public Level(LevelManager manager, File levelDir, String providerName) {
        this.manager = manager;
        this.name = levelDir.getName();

        generator = new FlatGenerator(); //TODO: correct generator

        switch (providerName) {
            case "levelDB":
                provider = new LevelDBProvider(this, new File(levelDir + File.separator + "db"));
                break;
            default:
                manager.getServer().getLogger().error("Could not resolve providerName: "+providerName);
                throw new LevelLoadException("Could not resolve providerName: "+providerName);
        }

        try {
            provider.loadLevelData(new File(levelDir + File.separator + "level.dat"));
        } catch (IOException e) {
            manager.getServer().getLogger().error("Failed to load Level: "+name);
            throw new LevelLoadException(e);
        }
    }

    public Chunk getChunkAt(ChunkPosition position) {
        for(Chunk c : loadedChunks) {
            if(c.getPosition().equals(position)) {
                return c;
            }
        }
        Chunk c = provider.getChunk(position);
        loadedChunks.add(c);
        return c;
    }

    public void loadChunk(ChunkPosition position) {
        for(Chunk c : loadedChunks) {
            if(c.getPosition().equals(position)) {
                throw new IllegalArgumentException("Chunk "+position+" already loaded!");
            }
        }
        Chunk c = provider.getChunk(position);
        loadedChunks.add(c);
    }

    public void unloadChunk(ChunkPosition position) {
        loadedChunks.stream().filter(c -> c.getPosition().equals(position)).forEach(loadedChunks::remove);
    }

    public LevelManager getManager() {
        return manager;
    }

    public String getName() {
        return name;
    }

    public void setSpawnPosition(Position spawnPosition) {
        this.spawnPosition = spawnPosition;
    }

    public void setGamemode(int gamemode) {
        this.gamemode = gamemode;
    }

    public void setTime(long time) {
        this.time = (int) time;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Generator getGenerator() {
        return generator;
    }

    public Position getSpawnPosition() {
        return spawnPosition;
    }
}
