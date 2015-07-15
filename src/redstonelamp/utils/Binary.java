package redstonelamp.utils;

import redstonelamp.entity.Entity;
import redstonelamp.entity.EntityMetadata;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

/**
 * Binary class for reading/writing values. USE INSTEAD OF JRAKLIB's BINARY!
 * <br>
 * If you are using BIG_ENDIAN order, please use <code>Binary.getDefaultInstance()</code> instead.
 */
public class Binary {
    private static Binary DEFAULT = Binary.newInstance(ByteOrder.BIG_ENDIAN);
    private ByteOrder order;

    private Binary(){}

    public static Binary newInstance(ByteOrder order){
        Binary binary = new Binary();
        binary.order = order;
        return binary;
    }

    public static Binary getDefaultInstance(){
        return DEFAULT;
    }

    public  byte writeByte(byte b) {
        return b;
    }

    public byte[] writeShort(short s) {
        return ByteBuffer.allocate(2).order(order).putShort(s).array();
    }

    public byte[] writeUnsignedShort(int us) {
        return ByteBuffer.allocate(2).order(order).putChar((char) us).array();
    }

    public byte[] writeInt(int i) {
        return ByteBuffer.allocate(4).order(order).putInt(i).array();
    }

    public byte[] writeLong(long l) {
        return ByteBuffer.allocate(8).order(order).putLong(l).array();
    }

    public byte[] writeFloat(float f) {
        return ByteBuffer.allocate(4).order(order).putFloat(f).array();
    }

    public byte[] writeDouble(double d) {
        return ByteBuffer.allocate(8).order(order).putDouble(d).array();
    }

    public byte[] writeLTriad(int t) {
        return net.beaconpe.jraklib.Binary.writeLTriad(t);
    }

    public byte[] writeChar(char c) {
        return ByteBuffer.allocate(2).order(order).putChar(c).array();
    }

    public byte[] writeMetadata(EntityMetadata metadata){
        return writeMetadata(metadata.getArray());
    }

    public byte[] writeMetadata(Map<Byte, List<Object>> array){
        DynamicByteBuffer bb = DynamicByteBuffer.newInstance(order);
        for(Byte bottom : array.keySet()){
            List<Object> d = array.get(bottom);
            EntityMetadata.DataType type = (EntityMetadata.DataType) d.get(0);
            bb.putByte((byte) ((type.getAsByte() << 5) | (bottom & 0x1F)));
            switch (type){
                case DATA_TYPE_BYTE:
                    bb.putByte((byte) d.get(1));
                    break;

                case DATA_TYPE_SHORT:
                    bb.setByteOrder(ByteOrder.LITTLE_ENDIAN);
                    bb.putShort((short) d.get(1));
                    bb.setByteOrder(order);
                    break;

                case DATA_TYPE_INT:
                    bb.setByteOrder(ByteOrder.LITTLE_ENDIAN);
                    bb.putInt((int) d.get(1));
                    bb.setByteOrder(order);
                    break;

                case DATA_TYPE_FLOAT:
                    bb.setByteOrder(ByteOrder.LITTLE_ENDIAN);
                    bb.putFloat((float) d.get(1));
                    bb.setByteOrder(order);
                    break;

                case DATA_TYPE_STRING:
                    bb.setByteOrder(ByteOrder.LITTLE_ENDIAN);
                    bb.putString((String) d.get(1));
                    bb.setByteOrder(order);
                    break;

                case DATA_TYPE_SLOT:
                    bb.setByteOrder(ByteOrder.LITTLE_ENDIAN);
                    List<Object> d2 = (List<Object>) d.get(1);
                    bb.putShort((short) d2.get(0));
                    bb.putByte((byte) d2.get(1));
                    bb.putShort((short) d2.get(2));
                    bb.setByteOrder(order);
                    break;

                case DATA_TYPE_POS:
                    bb.setByteOrder(ByteOrder.LITTLE_ENDIAN);
                    List<Object> d3 = (List<Object>) d.get(1);
                    bb.putInt((int) d3.get(0));
                    bb.putInt((int) d3.get(1));
                    bb.putInt((int) d3.get(2));
                    bb.setByteOrder(order);
                    break;

                case DATA_TYPE_LONG:
                    bb.setByteOrder(ByteOrder.LITTLE_ENDIAN);
                    bb.putLong((long) d.get(1));
                    bb.setByteOrder(order);
                    break;
            }
        }
        bb.putByte((byte) 0x7f);
        return bb.toArray();
    }

    public byte readByte(byte b) {
        return b;
    }

    public int readUnsignedByte(byte b) {
        return b & 0xFF;
    }

    public short readShort(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(order).getShort();
    }

    public int readUnsignedShort(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(order).getShort() & 0xFFFF;
    }

    public int readInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(order).getInt();
    }

    public long readLong(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(order).getLong();
    }

    public float readFloat(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(order).getFloat();
    }

    public double readDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(order).getDouble();
    }

    public int readLTriad(byte[] bytes) {
        return net.beaconpe.jraklib.Binary.readLTriad(bytes);
    }

    public char readChar(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(order).getChar();
    }

    public EntityMetadata readMetadata(byte[] bytes){
        return readMetadata(bytes, false);
    }

    public EntityMetadata readMetadata(byte[] bytes, boolean types){
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.order(order);
        EntityMetadata metadata = new EntityMetadata();

        byte b = bb.get();
        while(b != 127){
            byte bottom = (byte) (b & 0x1F);
            byte type = (byte) (b >> 5);
            Object r = null;
            switch(type){
                case 0:
                    r = bb.get();
                    break;

                case 1:
                    bb.order(ByteOrder.LITTLE_ENDIAN);
                    r = bb.getShort();
                    bb.order(order);
                    break;

                case 2:
                    bb.order(ByteOrder.LITTLE_ENDIAN);
                    r = bb.getInt();
                    bb.order(order);
                    break;

                case 3:
                    bb.order(ByteOrder.LITTLE_ENDIAN);
                    r = bb.getFloat();
                    bb.order(order);
                    break;

                case 4:
                    bb.order(ByteOrder.LITTLE_ENDIAN);
                    r = new String(new byte[bb.getShort()]);
                    bb.order(order);
                    break;

                case 5:
                    bb.order(ByteOrder.LITTLE_ENDIAN);
                    List<Object> list = new ArrayList<>();
                    list.add(bb.getShort());
                    list.add(bb.get());
                    list.add(bb.getShort());
                    bb.order(order);
                    r = list;
                    break;

                case 6:
                    bb.order(ByteOrder.LITTLE_ENDIAN);
                    List<Integer> list2 = new ArrayList<>();
                    for(int i = 0; i < 3; i++){
                        list2.add(bb.getInt());
                    }
                    bb.order(order);
                    r = list2;
                    break;

                case 8:
                    bb.order(order);
                    r = bb.getLong();
                    bb.order(order);
                    break;
            }
            if(types){
                metadata.set(bottom, Arrays.asList(new Object[] {r, type}));
            } else {
                metadata.set(bottom, Arrays.asList(new Object[] {r}));
            }
            b = bb.get();
        }

        return metadata;
    }
}
