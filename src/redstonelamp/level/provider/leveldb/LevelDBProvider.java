package redstonelamp.level.provider.leveldb;

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
import redstonelamp.io.InvalidDataException;
import redstonelamp.level.Chunk;
import redstonelamp.level.Level;
import redstonelamp.level.LevelProvider;
import redstonelamp.level.generator.Generator;
import redstonelamp.level.location.ChunkLocation;
import redstonelamp.level.location.Location;
import redstonelamp.utils.DynamicByteBuffer;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Level provider for the Level-DB format.
 *
 * @author jython234
 */
public class LevelDBProvider implements LevelProvider{
    private Level level;
    private Generator generator;
    private DB db;

    private CompoundTag levelData;

    public LevelDBProvider(Level level, Generator gen, File dbLocation){
        this.level = level;
        generator = gen;

        level.getServer().getLogger().info("Loading level "+level.getName()+" (FORMAT: LevelDB)");
        Options options = new Options();
        options.createIfMissing(true);
        options.compressionType(CompressionType.ZLIB);
        try {
            db = Iq80DBFactory.factory.open(dbLocation, options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadLevelData(File dataFile) throws IOException{
        if(!dataFile.exists()){
            level.getServer().getLogger().info("Couldn't find level.dat, creating new...");
            genNewLevelData(dataFile);
        }
        byte[] data = FileUtils.readFileToByteArray(dataFile);
        ByteArrayInputStream bytein = new ByteArrayInputStream(data);
        bytein.skip(8); //Skip header
        NBTInputStream in = new NBTInputStream(bytein, false, true);
        Tag tag = in.readTag();
        if(!(tag instanceof CompoundTag)){
            throw new InvalidDataException("Invalid level.dat!");
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

        level.setSpawnLocation(new Location(x, y, z, level));
        level.setGamemode((int) getTag("GameType").getValue());
        level.setTime((long) getTag("Time").getValue());
    }

    private void genNewLevelData(File dataFile) throws IOException {
        IntTag x = new IntTag("SpawnX", 128);
        IntTag y = new IntTag("SpawnY", 2); //TODO: Correct positions
        IntTag z = new IntTag("SpawnZ", 128);
        IntTag gamemode = new IntTag("GameType", 1);
        LongTag time = new LongTag("Time", 1);

        CompoundTag c = new CompoundTag("world data", Arrays.asList(new Tag[] {x, y, z, gamemode, time}));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(new byte[8]); //Fake header
        NBTOutputStream nbt = new NBTOutputStream(out, false, true);
        nbt.writeTag(c);
        nbt.close();

        FileUtils.writeByteArrayToFile(dataFile, out.toByteArray());
    }

    public Tag getTag(String name){
        for(Tag t : levelData.getValue()){
            if(t.getName().equals(name)){
                return t;
            }
        }
        return null;
    }

    @Override
    @Deprecated
    public synchronized byte[] orderChunk(int x, int z) {
        byte[] key = Key.getKey(x, z, KeyType.TERRAIN_DATA);
        byte[] data = db.get(key);
        if(data == null){
            Chunk c = generator.generateChunk(x, z);
            ByteBuffer bb = ByteBuffer.allocate(83200);
            bb.put(c.getBlockIds());
            bb.put(c.getBlockMeta());
            bb.put(c.getSkylight());
            bb.put(c.getBlocklight());
            bb.put(c.getHeightmap());
            bb.put(c.getBiomeColors());
            data = bb.array();
            db.put(key, data);
        }
        return data; //The MCPE format stores in Network-ready format! :)
    }

    @Override
    public Chunk getChunk(ChunkLocation chunkLocation) {
        byte[] key = Key.getKey(chunkLocation.getX(), chunkLocation.getZ(), KeyType.TERRAIN_DATA);
        byte[] data = db.get(key);
        if(data == null){
            Chunk c = generator.generateChunk(chunkLocation.getX(), chunkLocation.getZ());
            ByteBuffer bb = ByteBuffer.allocate(83200);
            bb.put(c.getBlockIds());
            bb.put(c.getBlockMeta());
            bb.put(c.getSkylight());
            bb.put(c.getBlocklight());
            bb.put(c.getHeightmap());
            bb.put(c.getBiomeColors());
            data = bb.array();
            db.put(key, data);
            return c;
        }
        DynamicByteBuffer bb = DynamicByteBuffer.newInstance(data);
        Chunk c = new Chunk();
        c.setBlockIds(bb.get(16 * 16 * 128));
        c.setBlockMeta(bb.get(16384));
        c.setSkylight(bb.get(16384));
        c.setBlocklight(bb.get(16384));
        c.setHeightmap(bb.get(256));
        c.setBiomeColors(bb.get(1024));
        return c;
    }

    @Override
    public void shutdown() {
        try {
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
