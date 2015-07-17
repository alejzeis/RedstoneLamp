package redstonelamp.item;

/**
 * Base class for all items.
 */
public class Item {
    private int id;
    private short metadata;
    private int count;

    public Item(int id, short metadata, int count){
        this.id = id;
        this.metadata = metadata;
        this.count = count;
    }

    public int getId() {
        return id;
    }

    public short getMetadata() {
        return metadata;
    }

    public void setMetadata(short metadata) {
        this.metadata = metadata;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
