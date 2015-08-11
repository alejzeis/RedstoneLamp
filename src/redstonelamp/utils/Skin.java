package redstonelamp.utils;

/**
 * Wrapper skin class.
 *
 * @author jython234
 */
public class Skin {
    private byte[] skin;

    public Skin(byte[] bytes){
        this.skin = bytes;
    }

    public void writeToDBB(DynamicByteBuffer bb){
        bb.putShort((short) skin.length);
        bb.put(skin);
    }

    public static Skin readFromDBB(DynamicByteBuffer bb){
        return new Skin(bb.get(bb.getUnsignedShort()));
    }

    public byte[] getBytes(){
        return skin;
    }
}
