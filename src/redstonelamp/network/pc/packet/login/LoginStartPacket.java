package redstonelamp.network.pc.packet.login;

import redstonelamp.network.pc.PCNetworkInfo;
import redstonelamp.network.pc.packet.PCDataPacket;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * LoginStart 0x00 (STATE: LOGIN, C -> S)
 *
 * @author jython234
 */
public class LoginStartPacket extends PCDataPacket{
    public final static int ID = PCNetworkInfo.LOGIN_LOGIN_START;

    public String name;

    @Override
    public int getID() {
        return PCNetworkInfo.LOGIN_LOGIN_START;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {

    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {
        name = bb.getPCString();
    }
}
