package redstonelamp.network.packet;

import redstonelamp.network.PENetworkInfo;
import redstonelamp.utils.DynamicByteBuffer;
import redstonelamp.utils.Skin;

/**
 * Login Packet (0x82)
 */
public class LoginPacket extends DataPacket{
    public static byte ID = PENetworkInfo.LOGIN_PACKET;

    public String username;
    public int protocol1;
    public int protocol2;
    public long clientId;

    public boolean slim = false;
    public Skin skin = null;

    @Override
    public byte getPID() {
        return PENetworkInfo.LOGIN_PACKET;
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
        System.out.println(bb.getByteBuffer().remaining() - 2);
        skin = Skin.readFromDBB(bb);
    }
}
