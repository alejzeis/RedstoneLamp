package redstonelamp.network.packet;

import redstonelamp.network.PENetworkInfo;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * Created by jython234 on 8/11/2015.
 *
 * @author jython234
 */
public class MovePlayerPacket extends DataPacket{
    public final static byte ID = PENetworkInfo.MOVE_PLAYER_PACKET;

    public final static byte MODE_NORMAL = 0;
    public final static byte MODE_RESET = 1;
    public final static byte MODE_ROTATION = 2;

    public long eid;
    public float x;
    public float y;
    public float z;
    public float yaw;
    public float bodyYaw;
    public float pitch;
    public byte mode = MODE_NORMAL;
    public boolean onGround;

    @Override
    public byte getPID() {
        return PENetworkInfo.MOVE_PLAYER_PACKET;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putLong(eid);
        bb.putFloat(x);
        bb.putFloat(y);
        bb.putFloat(z);
        bb.putFloat(yaw);
        bb.putFloat(bodyYaw);
        bb.putFloat(pitch);
        bb.putByte(mode);
        bb.putByte((byte) (onGround ? 1 : 0));
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {
        eid = bb.getLong();
        x = bb.getFloat();
        y = bb.getFloat();
        z = bb.getFloat();
        yaw = bb.getFloat();
        bodyYaw = bb.getFloat();
        pitch = bb.getFloat();
        mode = bb.getByte();
        onGround = bb.getByte() > 0;
    }
}
