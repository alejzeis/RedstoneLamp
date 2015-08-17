package redstonelamp.level.provider.leveldb;

/**
 * Created by jython234 on 8/16/2015.
 *
 * @author jython234
 */
public enum KeyType {
    TERRAIN_DATA((byte) 0x30),
    TILE_ENTITY_DATA((byte) 0x31),
    ENTITY_DATA((byte) 0x32),
    ONE_BYTE_DATA((byte) 0x76);

    private byte type;

    private KeyType(byte type){
        this.type = type;
    }

    public byte asByte() {
        return type;
    }
}
