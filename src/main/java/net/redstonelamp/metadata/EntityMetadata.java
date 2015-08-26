package net.redstonelamp.metadata;

import net.redstonelamp.nio.BinaryBuffer;

import java.nio.ByteOrder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * EntityMetadata class.
 *
 * @author RedstoneLamp Team
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

    public static byte[] write(EntityMetadata metadata) {
        BinaryBuffer bb = BinaryBuffer.newInstance(0, ByteOrder.LITTLE_ENDIAN);
        Map<Byte, List<Object>> array = metadata.getArray();
        for(Byte bottom : array.keySet()){
            List<Object> d = array.get(bottom);
            EntityMetadata.DataType type = (EntityMetadata.DataType) d.get(0);
            bb.putByte((byte) ((type.getAsByte() << 5) | (bottom & 0x1F)));
            switch (type){
                case DATA_TYPE_BYTE:
                    bb.putByte((byte) d.get(1));
                    break;

                case DATA_TYPE_SHORT:
                    bb.putShort((short) d.get(1));
                    break;

                case DATA_TYPE_INT:
                    bb.putInt((int) d.get(1));
                    break;

                case DATA_TYPE_FLOAT:
                    bb.putFloat((float) d.get(1));
                    break;

                case DATA_TYPE_STRING:
                    bb.putString((String) d.get(1));
                    break;

                case DATA_TYPE_SLOT:
                    List<Object> d2 = (List<Object>) d.get(1);
                    bb.putShort((short) d2.get(0));
                    bb.putByte((byte) d2.get(1));
                    bb.putShort((short) d2.get(2));
                    break;

                case DATA_TYPE_POS:
                    List<Object> d3 = (List<Object>) d.get(1);
                    bb.putInt((int) d3.get(0));
                    bb.putInt((int) d3.get(1));
                    bb.putInt((int) d3.get(2));
                    break;

                case DATA_TYPE_LONG:
                    bb.putLong((long) d.get(1));
                    break;
            }
        }
        bb.putByte((byte) 0x7f);
        return bb.toArray();
    }

    public Map<Byte, List<Object>> getArray() {
        return array;
    }

    public void setArray(Map<Byte, List<Object>> array) {
        this.array = array;
    }

    public boolean containsKey(int id) {
        return array.containsKey(id);
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
