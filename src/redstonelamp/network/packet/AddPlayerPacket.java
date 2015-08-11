package redstonelamp.network.packet;

import redstonelamp.entity.EntityMetadata;
import redstonelamp.network.PENetworkInfo;
import redstonelamp.utils.Binary;
import redstonelamp.utils.DynamicByteBuffer;
import redstonelamp.utils.Skin;

/**
 * AddPlayerPacket (0x88)
 *
 * @author jython234
 */
public class AddPlayerPacket extends DataPacket{
    public final static byte ID = PENetworkInfo.ADD_PLAYER_PACKET;

    public long clientID;
    public String username;
    public long eid;
    public float x;
    public float y;
    public float z;
    public float speedX;
    public float speedY;
    public float speedZ;
    public float yaw;
    public float pitch;
    public short item;
    public short meta;

    public boolean slim;
    public Skin skin;

    public EntityMetadata metadata;

    @Override
    public byte getPID() {
        return PENetworkInfo.ADD_PLAYER_PACKET;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putLong(clientID);
        bb.putString(username);
        bb.putLong(eid);
        bb.putFloat(x);
        bb.putFloat(y);
        bb.putFloat(z);
        bb.putFloat(speedX);
        bb.putFloat(speedY);
        bb.putFloat(speedZ);
        bb.putFloat(yaw);
        bb.putFloat(yaw); //TODO: head-rotation
        bb.putFloat(pitch);
        bb.putShort(item);
        bb.putShort(meta);
        bb.putByte(slim ? (byte) 1 : (byte) 0);
        skin.writeToDBB(bb);
        bb.put(Binary.getDefaultInstance().writeMetadata(metadata));
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {

    }
}
