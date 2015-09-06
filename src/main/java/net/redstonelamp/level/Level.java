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

import net.redstonelamp.block.Block;
import net.redstonelamp.entity.EntityManager;
import net.redstonelamp.level.generator.Generator;
import net.redstonelamp.level.position.BlockPosition;
import net.redstonelamp.level.position.Position;
import net.redstonelamp.level.provider.LevelLoadException;
import net.redstonelamp.level.provider.LevelProvider;
import net.redstonelamp.math.Vector3;
import net.redstonelamp.metadata.EntityMetadata;
import net.redstonelamp.response.BlockPlaceResponse;
import net.redstonelamp.response.RemoveBlockResponse;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

/**
 * Represents a Level in a World
 *
 * @author RedstoneLamp Team
 */
public class Level{
    private final LevelManager manager;
    private final EntityManager entityManager;
    private List<Chunk> loadedChunks = new CopyOnWriteArrayList<>();
    private Queue<BlockPlaceResponse> blockPlaceQueue = new ArrayBlockingQueue<>(16); //Able to process 16 block place requests per tick
    private Queue<RemoveBlockResponse> removeBlockQueue = new ArrayBlockingQueue<>(16); //Able to process 16 block remove requests per tick
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
        }catch(NullPointerException e){
            throw new LevelLoadException("Unknown level provider " + providerName);
        }catch(InvocationTargetException e){
            throw new LevelLoadException(e.getTargetException());
        }catch(IllegalAccessException | InstantiationException e){
            throw new LevelLoadException(e);
        }
        try{
            generator = manager.getGenerator(generatorName).newInstance(this, params);
        }catch(NullPointerException e){
            throw new LevelLoadException("Unknown level generator " + providerName);
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
        entityManager = new EntityManager(this);
        name = provider.getName();
    }

    public void tick() {
        sendBlockQueues();
    }

    private void sendBlockQueues() {
        if(!blockPlaceQueue.isEmpty()) {
            manager.getServer().broadcastResponses(blockPlaceQueue.toArray(new BlockPlaceResponse[blockPlaceQueue.size()]));
            blockPlaceQueue.clear();
        }
        if(!removeBlockQueue.isEmpty()) {
            manager.getServer().broadcastResponses(removeBlockQueue.toArray(new RemoveBlockResponse[removeBlockQueue.size()]));
            removeBlockQueue.clear();
        }
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
        loadedChunks.stream().filter(c -> c.getPosition().equals(position)).forEach(chunk -> {
            provider.putChunk(position, chunk);
            loadedChunks.remove(chunk);
        });
    }

    public void save() {
        for(Chunk c : loadedChunks) {
            provider.putChunk(c.getPosition(), c);
        }
    }

    public boolean isChunkLoaded(ChunkPosition position) {
        for(Chunk c : loadedChunks) {
            if(c.getPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }

    public void setBlock(BlockPosition position, Block block) {
        Chunk c = getChunkAt(new ChunkPosition(position.getX() / 16, position.getZ() / 16));
        c.setBlockId((byte) block.getId(), position.getX() & 0xf, position.getY() & 0x7f, position.getZ() & 0xf);
        c.setBlockMeta((byte) block.getMeta(), position.getX() & 0xf, position.getY() & 0x7f, position.getZ() & 0xf);
        if(!blockPlaceQueue.offer(new BlockPlaceResponse(block, position))) {
            //Queue is full, send immediately then
            sendBlockQueues();
            blockPlaceQueue.add(new BlockPlaceResponse(block, position));
        }
    }

    public void removeBlock(BlockPosition position) {
        Chunk c = getChunkAt(new ChunkPosition(position.getX() / 16, position.getZ() / 16));
        c.setBlockId((byte) 0, position.getX() & 0xf, position.getY() & 0x7f, position.getZ() & 0xf); //Set block to AIR
        c.setBlockMeta((byte) 0, position.getX() & 0xf, position.getY() & 0x7f, position.getZ() & 0xf);
        if(!removeBlockQueue.offer(new RemoveBlockResponse(position))) {
            //Queue is full, send immediately then
            sendBlockQueues();
            removeBlockQueue.add(new RemoveBlockResponse(position));
        }
    }

    public Block getBlock(BlockPosition position) {
        Chunk c = getChunkAt(new ChunkPosition(position.getX() / 16, position.getZ() / 16));
        byte id = c.getBlockId(position.getX() & 0xf, position.getY() & 0x7f, position.getZ() & 0xf);
        byte meta = c.getBlockMeta(position.getX() & 0xf, position.getY() & 0x7f, position.getZ() & 0xf);
        return new Block(id, meta, 1);
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

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public static class LevelParameters{
        public String name;
        public File levelDir;
    }
}
