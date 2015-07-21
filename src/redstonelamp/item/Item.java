package redstonelamp.item;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Base class for all items.
 */
public class Item implements ItemValues{
    private static Map<Integer, Class<? extends Item>> list = new ConcurrentHashMap<>();
    private static Map<Integer, Class<? extends Item>> creative = new ConcurrentHashMap<>();

    private int id;
    private short metadata;
    private int count;

    public Item(int id, short metadata, int count){
        this.id = id;
        this.metadata = metadata;
        this.count = count;
    }

    public static void initCreativeItems(){

    }

    public static Item get(int id, short metadata, int count){
        if(list.containsKey(id)){
            Class<? extends Item> clazz = list.get(id);
            try {
                return clazz.getConstructor(int.class, short.class, int.class).newInstance(id, metadata, count);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if(id < 256){
            //TODO: ItemBlock
        } else {
            return new Item(id, metadata, count);
        }
        return null;
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
