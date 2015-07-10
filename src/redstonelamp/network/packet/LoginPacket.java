package redstonelamp.network.packet;

import redstonelamp.network.NetworkInfo;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * Login Packet (0x82)
 */
public class LoginPacket extends DataPacket{
    public static byte ID = NetworkInfo.LOGIN_PACKET;

    public String username;
    public int protocol1;
    public int protocol2;
    public long clientId;

    public boolean slim = false;
    public String skin = null;

    @Override
    public byte getPID() {
        return NetworkInfo.LOGIN_PACKET;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {

    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {
        System.out.println(bb.getPosition());
        username = bb.getString();
        protocol1 = bb.getInt();
        protocol2 = bb.getInt();
        clientId = bb.getInt();

        slim = bb.getByte() > 0;
        skin = bb.getString();
    }
}
