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

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ChunkLocation){
            int x = ((ChunkLocation) obj).getX();
            int z = ((ChunkLocation) obj).getZ();
            return (x == this.x) && (z == this.z);
        } else {
            return obj.equals(this);
        }
    }

    @Override
    public String toString() {
        return "ChunkLocation: "+x+", "+z;
    }
}
