/*
 * This file is part of RedstoneLamp.
 *
 * RedstoneLamp is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * RedstoneLamp is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with RedstoneLamp.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.redstonelamp.level;

import net.redstonelamp.level.generator.FlatGenerator;
import net.redstonelamp.level.generator.Generator;
import net.redstonelamp.level.position.Position;
import net.redstonelamp.level.provider.LevelLoadException;
import net.redstonelamp.level.provider.LevelProvider;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Represents a Level in a World
 *
 * @author RedstoneLamp Team
 */
public class Level{
    private final LevelManager manager;
    private List<Chunk> loadedChunks = new CopyOnWriteArrayList<>();
    private LevelProvider provider;

    private String name;
    private Position spawnPosition;
    private int gamemode;
    private int time;
    private Generator generator;

    public Level(LevelManager manager, String providerName, String generatorName, LevelParameters params){
        this.manager = manager;

        try{
            provider = manager.getProvider(providerName).newInstance(this, params);
            generator = manager.getGenerator(generatorName).newInstance(this, params);
        }catch(NullPointerException e){
            throw new LevelLoadException("Unknown level provider " + providerName);
        }catch(InvocationTargetException e){
            throw new LevelLoadException(e.getTargetException());
        }catch(IllegalAccessException | InstantiationException e){
            throw new LevelLoadException(e);
        }
        try{
            provider.init();
        }catch(IOException e){
            manager.getServer().getLogger().error("Failed to load Level: " + name);
            throw new LevelLoadException(e);
        }
        name = provider.getName();
    }

    public Chunk getChunkAt(ChunkPosition position){
        for(Chunk c : loadedChunks){
            if(c.getPosition().equals(position)){
                return c;
            }
        }
        Chunk c = provider.getChunk(position);
        loadedChunks.add(c);
        return c;
    }

    public void loadChunk(ChunkPosition position){
        for(Chunk c : loadedChunks){
            if(c.getPosition().equals(position)){
                throw new IllegalArgumentException("Chunk " + position + " already loaded!");
            }
        }
        Chunk c = provider.getChunk(position);
        loadedChunks.add(c);
    }

    public void unloadChunk(ChunkPosition position){
        loadedChunks.stream().filter(c -> c.getPosition().equals(position)).forEach(loadedChunks::remove);
    }

    public LevelManager getManager(){
        return manager;
    }

    public String getName(){
        return name;
    }

    public void setSpawnPosition(Position spawnPosition){
        this.spawnPosition = spawnPosition;
    }

    public void setGamemode(int gamemode){
        this.gamemode = gamemode;
    }

    public void setTime(long time){
        this.time = (int) time;
    }

    public int getTime(){
        return time;
    }

    public void setTime(int time){
        this.time = time;
    }

    public Generator getGenerator(){
        return generator;
    }

    public Position getSpawnPosition(){
        return spawnPosition;
    }

    public static class LevelParameters{
        public String name;
        public File levelDir;
    }
}
