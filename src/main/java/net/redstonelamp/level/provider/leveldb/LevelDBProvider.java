/**
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
package net.redstonelamp.level.provider.leveldb;

import net.redstonelamp.level.Chunk;
import net.redstonelamp.level.ChunkPosition;
import net.redstonelamp.level.Level;
import net.redstonelamp.level.position.Position;
import net.redstonelamp.level.provider.LevelLoadException;
import net.redstonelamp.level.provider.LevelProvider;
import net.redstonelamp.nio.BinaryBuffer;
import org.apache.commons.io.FileUtils;
import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.spout.nbt.CompoundTag;
import org.spout.nbt.IntTag;
import org.spout.nbt.LongTag;
import org.spout.nbt.Tag;
import org.spout.nbt.stream.NBTInputStream;
import org.spout.nbt.stream.NBTOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * An implementation of a LevelProvider for the LevelDB platform.
 * This format is used by vanilla MCPE.
 *
 * @author RedstoneLamp Team
 */
public class LevelDBProvider implements LevelProvider{
    private final Level level;
    private final DB database;

    private CompoundTag levelData;

    /**
     * Create a new LevelDBProvider and load the database.
     * @param level The Level class that this provider provides to.
     * @param databaseDir The directory in which the database files are contained.
     *                    This is usually "db".
     */
    public LevelDBProvider(Level level, File databaseDir) {
        this.level = level;

        Options options = new Options();
        options.createIfMissing(true);
        options.compressionType(CompressionType.ZLIB);

        try {
            database = Iq80DBFactory.factory.open(databaseDir, options);
        } catch (IOException e) {
            level.getManager().getServer().getLogger().error(e.getClass().getName()+" while loading LevelDB world "+databaseDir.getName());
            throw new LevelLoadException(e);
        }
    }

    @Override
    public Chunk getChunk(ChunkPosition position) {
        byte[] key = Key.TYPE_TERRAIN_DATA.assembleKey(position);
        byte[] data = database.get(key);
        if(data == null){
            Chunk c = level.getGenerator().generateChunk(position);
            ByteBuffer bb = ByteBuffer.allocate(83200);
            bb.put(c.getBlockIds());
            bb.put(c.getBlockMeta());
            bb.put(c.getSkylight());
            bb.put(c.getBlocklight());
            bb.put(c.getHeightmap());
            bb.put(c.getBiomeColors());
            data = bb.array();
            database.put(key, data);
            return c;
        }
        BinaryBuffer bb = BinaryBuffer.wrapBytes(data, ByteOrder.BIG_ENDIAN);
        Chunk c = new Chunk(position);
        c.setBlockIds(bb.get(16 * 16 * 128));
        c.setBlockMeta(bb.get(16384));
        c.setSkylight(bb.get(16384));
        c.setBlocklight(bb.get(16384));
        c.setHeightmap(bb.get(256));
        c.setBiomeColors(bb.get(1024));
        return c;
    }

    private void genNewLevelData(File file) throws IOException {
        IntTag x = new IntTag("SpawnX", 128);
        IntTag y = new IntTag("SpawnY", 3); //TODO: Correct positions
        IntTag z = new IntTag("SpawnZ", 128);
        IntTag gamemode = new IntTag("GameType", 1);
        LongTag time = new LongTag("Time", 1);

        CompoundTag c = new CompoundTag("world data", Arrays.asList(new Tag[]{x, y, z, gamemode, time}));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(new byte[8]); //Fake header
        NBTOutputStream nbt = new NBTOutputStream(out, false, true);
        nbt.writeTag(c);
        nbt.close();

        FileUtils.writeByteArrayToFile(file, out.toByteArray());
    }

    @Override
    public void shutdown() {
        try {
            database.close();
        } catch (IOException e) {
            level.getManager().getServer().getLogger().warning("Failed to close LevelDB database for level "+level.getName());
        }
    }

    @Override
    public void loadLevelData(File file) throws IOException {
        if(!file.exists()){
            level.getManager().getServer().getLogger().info("Couldn't find level.dat, creating new...");
            genNewLevelData(file);
        }
        byte[] data = FileUtils.readFileToByteArray(file);
        ByteArrayInputStream bytein = new ByteArrayInputStream(data);
        bytein.skip(8); //Skip header
        NBTInputStream in = new NBTInputStream(bytein, false, true);
        Tag tag = in.readTag();
        if(!(tag instanceof CompoundTag)){
            throw new LevelLoadException("Invalid level.dat!");
        }
        in.close();

        levelData = (CompoundTag) tag;
        /*
        for(int i = 0; i < levelData.getValue().size(); i++){
            Tag t = levelData.getValue().get(i);
            System.out.println(i+" "+t.getName()+", "+t.getValue());
        }
        */

        int x = (int) getTag("SpawnX").getValue();
        int y = (int) getTag("SpawnY").getValue();
        int z = (int) getTag("SpawnZ").getValue();

        level.setSpawnPosition(new Position(x, y, z, level));
        level.setGamemode((int) getTag("GameType").getValue());
        level.setTime((long) getTag("Time").getValue());
    }

    public Tag getTag(String name){
        for(Tag t : levelData.getValue()){
            if(t.getName().equals(name)){
                return t;
            }
        }
        return null;
    }
}
