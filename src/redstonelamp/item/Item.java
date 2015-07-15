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
}
