package redstonelamp.level;

/**
 * Represents a 16 * 16 * 128 chunk section.
 */
public class Chunk {

    private int chunkX;
    private int chunkZ;

    private byte[] blockIds;
    private byte[] blockMeta;
    private byte[] skylight;
    private byte[] blocklight;
    private byte[] heightmap;
    private byte[] biomeColors;

    public Chunk(){}

    public Chunk(byte[] blockIds, byte[] blockMeta, byte[] skylight, byte[] blocklight, byte[] heightmap, byte[] biomeColors){
        this.blockIds = blockIds;
        this.blockMeta = blockMeta;
        this.skylight = skylight;
        this.blocklight = blocklight;
        this.heightmap = heightmap;
        this.biomeColors = biomeColors;
    }

    public byte[] getBiomeColors() {
        return biomeColors;
    }

    public byte[] getHeightmap() {
        return heightmap;
    }

    public byte[] getBlocklight() {
        return blocklight;
    }

    public byte[] getSkylight() {
        return skylight;
    }

    public byte[] getBlockMeta() {
        return blockMeta;
    }

    public byte[] getBlockIds() {
        return blockIds;
    }

    public void setBlockIds(byte[] blockIds) {
        this.blockIds = blockIds;
    }

    public void setBlockMeta(byte[] blockMeta) {
        this.blockMeta = blockMeta;
    }

    public void setSkylight(byte[] skylight) {
        this.skylight = skylight;
    }

    public void setBlocklight(byte[] blocklight) {
        this.blocklight = blocklight;
    }

    public void setHeightmap(byte[] heightmap) {
        this.heightmap = heightmap;
    }

    public void setBiomeColors(byte[] biomeColors) {
        this.biomeColors = biomeColors;
    }
}
