package redstonelamp.entity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by jython234 on 7/14/2015.
 */
public class EntityMetadata {
    private Map<Byte, List<Object>> array = new ConcurrentHashMap<>();

    public List<Object> get(Byte key){
        return array.get(key);
    }

    public EntityMetadata set(Byte key, List<Object> value){
        array.put(key, value);
        return this;
    }

    public EntityMetadata add(Byte key, Object value){
        if(!array.containsKey(key)){
            List<Object> l = new CopyOnWriteArrayList<>();
            l.add(value);
            return set(key, l);
        }
        List<Object> list = array.get(key);
        list.add(value);
        array.put(key, list);
        return this;
    }

    public Map<Byte, List<Object>> getArray() {
        return array;
    }

    public void setArray(Map<Byte, List<Object>> array) {
        this.array = array;
    }


    public static enum DataType{
        DATA_TYPE_BYTE((byte) 0),
        DATA_TYPE_SHORT((byte) 1),
        DATA_TYPE_INT((byte) 2),
        DATA_TYPE_FLOAT((byte) 3),
        DATA_TYPE_STRING((byte) 4),
        DATA_TYPE_SLOT((byte) 5),
        DATA_TYPE_POS((byte) 6),
        DATA_TYPE_ROTATION((byte) 7),
        DATA_TYPE_LONG((byte) 8);

        private byte type;

        private DataType(byte type){
            this.type = type;
        }

        public byte getAsByte(){
            return type;
        }
    }
}
