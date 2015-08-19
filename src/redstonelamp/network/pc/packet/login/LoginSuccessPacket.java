package redstonelamp.network.pc.packet.login;

import redstonelamp.network.pc.PCNetworkInfo;
import redstonelamp.network.pc.packet.PCDataPacket;
import redstonelamp.utils.DynamicByteBuffer;

import java.util.UUID;

/**
 * LoginSuccessPacket 0x02 (STATE: LOGIN, S -> C)
 *
 * @author jython234
 */
public class LoginSuccessPacket extends PCDataPacket{
    public final static int ID = PCNetworkInfo.LOGIN_LOGIN_SUCCESS;

    public UUID uuid;
    public String username;

    @Override
    public int getID() {
        return PCNetworkInfo.LOGIN_LOGIN_SUCCESS;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putPCString(uuid.toString());
        bb.putPCString(username);
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {

    }
}
