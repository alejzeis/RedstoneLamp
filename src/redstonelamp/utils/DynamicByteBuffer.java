package redstonelamp.utils;


import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Represents a dynamically growing byte buffer.
 */
public class DynamicByteBuffer {
    private Binary binary;
    private ByteBuffer bb;

    private DynamicByteBuffer() { }

    /**
     * Create a new DynamicByteBuffer wrapped around <code>bytes</code>.
     * @param bytes The byte[] to be wrapped around.
     * @return A new <code>DynamicByteBuffer</code> instance.
     */
    public static DynamicByteBuffer newInstance(byte[] bytes){
        DynamicByteBuffer bb = new DynamicByteBuffer();
        bb.setByteBuffer(ByteBuffer.wrap(bytes));
        bb.setByteOrder(ByteOrder.BIG_ENDIAN);
        bb.setPosition(0);
        bb.binary = Binary.getDefaultInstance();
        return bb;
    }

    /**
     * Create a new DynamicByteBuffer wrapped around <code>bytes</code> with specified ByteOrder <code>order</code>.
     * @param bytes The byte[] to be wrapped around.
     * @param order The ByteOrder to be used.
     * @return A new <code>DynamicByteBuffer</code> instance.
     */
    public static DynamicByteBuffer newInstance(byte[] bytes, ByteOrder order){
        DynamicByteBuffer bb = new DynamicByteBuffer();
        bb.setByteBuffer(ByteBuffer.wrap(bytes));
        bb.setByteOrder(order);
        bb.setPosition(0);
        bb.binary = Binary.newInstance(order);
        return bb;
    }

    /**
     * Create a new DynamicByteBuffer, ready for writing.
     * @return A new <code>DynamicByteBuffer</code> instance.
     */
    public static DynamicByteBuffer newInstance(){
        DynamicByteBuffer bb = new DynamicByteBuffer();
        bb.setByteBuffer(ByteBuffer.allocate(0));
        bb.setByteOrder(ByteOrder.BIG_ENDIAN);
        bb.setPosition(0);
        bb.binary = Binary.getDefaultInstance();
        return bb;
    }

    /**
     * Create a new DynamicByteBuffer, ready for writing, with specified ByteOrder <code>order</code>.
     * @return A new <code>DynamicByteBuffer</code> instance.
     */
    public static DynamicByteBuffer newInstance(ByteOrder order){
        DynamicByteBuffer bb = new DynamicByteBuffer();
        bb.setByteBuffer(ByteBuffer.allocate(0));
        bb.setByteOrder(order);
        bb.setPosition(0);
        bb.binary = Binary.newInstance(order);
        return bb;
    }

    /**
     * Put a byte array into the buffer. If the array is too big, the buffer will extend to cover the amount.
     * @param bytes The byte[]
     */
    public void put(byte[] bytes){
        try{
            bb.put(bytes);
        } catch(BufferOverflowException e){
            bb.position(0);
            byte[] data = new byte[bb.remaining()];
            bb.get(data);

            bb = ByteBuffer.allocate(data.length + bytes.length);
            bb.put(data);
            bb.put(bytes);
        }
    }

    /**
     * Get <code>len</code> amount of bytes from the buffer.
     * @param len Amount of bytes to get.
     * @return The bytes.
     */
    public byte[] get(int len){
        byte[] bytes = new byte[len];
        bb.get(bytes);
        return bytes;
    }

    /**
     * Puts a signed, 8-bit byte into the buffer.
     * @param b The byte
     */
    public void putByte(byte b){
        put(new byte[] {binary.writeByte(b)});
    }

    /**
     * Puts a signed, 16-bit short into the buffer.
     * @param s The short
     */
    public void putShort(short s){
        put(binary.writeShort(s));
    }

    /**
     * Puts an unsigned, 16 bit short into the buffer.
     * @param us
     */
    public void putUnsignedShort(int us){
        put(binary.writeUnsignedShort(us));
    }

    //TODO: Finish docs

    public void putInt(int i){
        put(binary.writeInt(i));
    }

    public void putVarInt(int i){
        put(binary.writeVarInt(i));
    }

    public void putLong(long l){
        put(binary.writeLong(l));
    }

    public void putFloat(float f){
        put(binary.writeFloat(f));
    }

    public void putDouble(double d){
        put(binary.writeDouble(d));
    }

    public void putLTriad(int t){
        put(binary.writeLTriad(t));
    }

    public void putString(String s){
        putShort((short) s.getBytes().length);
        put(s.getBytes());
    }

    public void putPCString(String s){
        putVarInt(s.getBytes(Charset.forName("UTF-8")).length);
        put(s.getBytes(Charset.forName("UTF-8")));
    }
    
    public void putChar(char c){
        put(binary.writeChar(c));
    }

    public byte getByte(){
        return binary.readByte(get(1)[0]);
    }

    public int getUnsignedByte(){
        return binary.readUnsignedByte(get(1)[0]);
    }
    
    public short getShort(){
        return binary.readShort(get(2));
    }
    
    public int getUnsignedShort(){
        return binary.readUnsignedShort(get(2));
    }
    
    public int getInt(){
        return binary.readInt(get(4));
    }

    public int getVarInt() {
        try {
            return binary.readVarInt(bb);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public long getLong(){
        return binary.readLong(get(8));
    }
    
    public float getFloat(){
        return binary.readFloat(get(4));
    }
    
    public double getDouble(){
        return binary.readDouble(get(8));
    }
    
    public int getLTriad(){
        return binary.readLTriad(get(3));
    }

    public String getString(){
        return getString(Charset.forName("UTF-8"));
    }

    public String getString(Charset charset){
        return new String(get(getUnsignedShort()), charset);
    }

    public String getPCString(){
        return new String(get(getVarInt()), Charset.forName("UTF-8"));
    }
    
    public char getChar(){
        return binary.readChar(get(2));
    }

    public byte[] toArray(){
        return bb.array();
    }

    public String toString(){
        return Arrays.toString(toArray());
    }
    
    public int getPosition(){
        return bb.position();
    }
    
    public void setPosition(int position){
        bb.position(position);
    }
    
    public ByteOrder getByteOrder(){
        return bb.order();
    }
    
    public void setByteOrder(ByteOrder order){
        bb.order(order);
        binary = Binary.newInstance(order);
    }
    
    public ByteBuffer getByteBuffer(){
        return bb;
    }

    private void setByteBuffer(ByteBuffer bb){
        this.bb = bb;
    }

    public byte[] getRemainingBytes() {
        return get(bb.remaining());
    }
}
