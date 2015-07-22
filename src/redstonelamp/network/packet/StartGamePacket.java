package redstonelamp.network.packet;

import redstonelamp.network.NetworkInfo;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * StartGamePacket (0x87)
 */
public class StartGamePacket extends DataPacket{
    public final static byte ID = NetworkInfo.START_GAME_PACKET;

    public int seed;
    public int generator;
    public int gamemode;
    public long eid;
    public int spawnX;
    public int spawnY;
    public int spawnZ;
    public float x;
    public float y;
    public float z;

    @Override
    public byte getPID() {
        return NetworkInfo.START_GAME_PACKET;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putInt(seed);
        bb.putInt(generator);
        bb.putInt(gamemode);
        bb.putLong(eid);
        bb.putInt(spawnX);
        bb.putInt(spawnY);
        bb.putInt(spawnZ);
        bb.putFloat(x);
        bb.putFloat(y);
        bb.putFloat(z);
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {

    }
}
