package redstonelamp.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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
}
