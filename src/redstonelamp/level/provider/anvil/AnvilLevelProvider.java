package redstonelamp.level.provider.anvil;

import com.lb_stuff.mcmodify.location.LocChunkInRegion;
import com.lb_stuff.mcmodify.location.LocRegionInDimension;
import com.lb_stuff.mcmodify.minecraft.Chunk;
import com.lb_stuff.mcmodify.minecraft.Dimension;
import com.lb_stuff.mcmodify.minecraft.FileRegion;
import com.lb_stuff.mcmodify.minecraft.World;
import redstonelamp.item.ItemValues;
import redstonelamp.level.Level;
import redstonelamp.level.LevelProvider;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Level provider for the Minecraft: Anvil level format.
 *
 * @author jython234
 */
public class AnvilLevelProvider implements LevelProvider{
    private Level level;
    private World world;

    public AnvilLevelProvider(Level level){
        this.level = level;
        world = new World(level.getDefaultWorldDataFolder());
    }

    /**
     * Order an anvil chunk to network format.
     * <br>
     * Based on code from the Dragonet project. Original code can be found here:
     * https://github.com/DragonetMC/Dragonet/blob/master/Dragonet-core/src/main/java/org/dragonet/net/ClientChunkManager.java#L192
     * @param cx
     * @param cz
     * @return
     */
    @Override
    public byte[] orderChunk(int cx, int cz) {
        try {
            FileRegion region = world.getFileRegion(Dimension.OVERWORLD, new LocRegionInDimension(cx / 32, cz / 32));
            Chunk c = region.getChunk(new LocChunkInRegion(cx, cz));
            if(c != null){
                ByteBuffer bb = ByteBuffer.allocate(83200);
                for(int blockX = 0; blockX < 16; blockX++){
                    for(int blockZ = 0; blockZ < 16; blockZ++){
                        for(int blockY = 0; blockY < 128; blockY++){
                            bb.put((byte) c.BlockID(blockX, blockY, blockZ));
                        }
                    }
                }
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = 0; y < 128; y += 2) {
                            byte data;
                            data = (byte) ((c.BlockData(x, y, z) & 0xF) << 4);
                            data |= c.BlockData(x, y + 1, z) & 0xF;
                            bb.put(data);
                        }
                    }
                }
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = 0; y < 128; y += 2) {
                            byte data;
                            data = (byte) ((c.SkyLight(x, y, z) & 0xF) << 4);
                            data |= c.SkyLight(x, y + 1, z) & 0xF;
                            bb.put(data);
                        }
                    }
                }
                for (int x = 0; x < 16; x++) {
                    for (int z = 0; z < 16; z++) {
                        for (int y = 0; y < 128; y += 2) {
                            byte data;
                            data = (byte) ((c.BlockLight(x, y, z) & 0xF) << 4);
                            data |= c.BlockLight(x, y + 1, z) & 0xF;
                            bb.put(data);
                        }
                    }
                }
                for(int i = 0; i < 256; i++){ //Heightmap
                    bb.put((byte) 0xFF);
                }
                for(int i = 0; i < 256; i++){ //Biome Colors
                    bb.put((byte) 0x01);
                    bb.put((byte) 0x85);
                    bb.put((byte) 0xB2);
                    bb.put((byte) 0x4A);
                }
                return bb.array();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    @Override
    public void shutdown() {

    }

    @Override
    public void loadLevelData(File file) throws IOException {
        //TODO
    }
}
