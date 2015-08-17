package redstonelamp.level.provider.leveldb;

import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;
import redstonelamp.level.Level;
import redstonelamp.level.LevelProvider;

import java.io.File;
import java.io.IOException;

/**
 * Level provider for the Level-DB format.
 *
 * @author jython234
 */
public class LevelDBProvider implements LevelProvider{
    private Level level;
    private DB db;

    public LevelDBProvider(Level level, File worldLocation){
        this.level = level;

        level.getServer().getLogger().info("Loading level "+level.getName()+" (FORMAT: LevelDB)");
        Options options = new Options();
        options.createIfMissing(true);
        options.compressionType(CompressionType.ZLIB);
        try {
            db = Iq80DBFactory.factory.open(worldLocation, options);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] orderChunk(int x, int z) {
        byte[] data = db.get(Key.getKey(x, z, KeyType.TERRAIN_DATA));
        return data; //The MCPE format stores in Network-ready format! :)
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
