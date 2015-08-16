package redstonelamp.network.pc.packet.login;

import redstonelamp.network.pc.Chat;
import redstonelamp.network.pc.PCNetworkInfo;
import redstonelamp.network.pc.packet.PCDataPacket;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * LoginDisconnectPacket 0x00 (STATE: LOGIN, S -> C)
 *
 * @author jython234
 */
public class LoginDisconnectPacket extends PCDataPacket{
    public final static int ID = PCNetworkInfo.LOGIN_DISCONNECT;

    public Chat message;

    @Override
    public int getID() {
        return PCNetworkInfo.LOGIN_DISCONNECT;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putPCString(message.toString());
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {

    }
}
