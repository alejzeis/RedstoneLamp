package redstonelamp.level.location;

/**
 * Represents a chunk location.
 */
public class ChunkLocation {
    private int x, z;

    public ChunkLocation(int x, int z){
        this.x = x;
        this.z = z;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
}
