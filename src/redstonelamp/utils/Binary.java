package redstonelamp.utils;

import redstonelamp.entity.EntityMetadata;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;
import java.util.regex.Pattern;

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

    public byte[] writeUUID(UUID id){
        String[] parts = id.toString().split(Pattern.quote("-"));
        ByteBuffer bb = ByteBuffer.allocate(16);
        bb.order(order);
        bb.put(hex2bytes(parts[0]));
        bb.put(hex2bytes(parts[1]));
        bb.put(hex2bytes(parts[2]));
        bb.put(hex2bytes(parts[3]));
        return bb.array();
    }

    /**
     * Writes a VarInt. Code is from: https://gist.github.com/zh32/7190955
     * @param i
     * @return
     */
    public byte[] writeVarInt(int i){
        DynamicByteBuffer bb = DynamicByteBuffer.newInstance();
        while (true) {
            if ((i & 0xFFFFFF80) == 0) {
                bb.putByte((byte) i);
                return bb.toArray();
            }

            bb.putByte((byte) (i & 0x7F | 0x80));
            i >>>= 7;
        }
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

    public UUID readUUID(byte[] bytes) {
        DynamicByteBuffer bb = DynamicByteBuffer.newInstance(bytes, order);
        String[] parts = new String[4];
        parts[0] = bytes2hex(bb.get(4));
        parts[1] = bytes2hex(bb.get(4));
        parts[2] = bytes2hex(bb.get(4));
        parts[3] = bytes2hex(bb.get(4));

        StringBuilder sb = new StringBuilder();
        sb.append(parts[0]+"-");
        sb.append(parts[1]+"-");
        sb.append(parts[2]+"-");
        sb.append(parts[3]+"-");
        return UUID.fromString(sb.toString());
    }

    /**
     * Reads a VarInt. Code is from: https://gist.github.com/zh32/7190955
     * @param bytes
     * @return
     */
    public int readVarInt(byte[] bytes) throws IOException {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        int i = 0;
        int j = 0;
        while (true) {
            int k = bb.get();
            i |= (k & 0x7F) << j++ * 7;
            if (j > 5) throw new RuntimeException("VarInt too big");
            if ((k & 0x80) != 128) break;
        }
        return i;
    }

    /**
     * Reads a VarInt using the given Bytebuffer. Code is from: https://gist.github.com/zh32/7190955
     * @param bytes
     * @return
     */
    public int readVarInt(ByteBuffer bb) throws IOException {
        int i = 0;
        int j = 0;
        while (true) {
            int k = bb.get();
            i |= (k & 0x7F) << j++ * 7;
            if (j > 5) throw new RuntimeException("VarInt too big");
            if ((k & 0x80) != 128) break;
        }
        return i;
    }

    public static String dumpHexBytes(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            sb.append(String.format("%02X", b)+" ");
        }
        return sb.toString();
    }

    public static String bytes2hex(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }

    public static byte[] hex2bytes(String s){
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
