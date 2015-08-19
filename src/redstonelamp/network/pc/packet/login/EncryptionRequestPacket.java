package redstonelamp.network.pc.packet.login;

import redstonelamp.network.pc.PCNetworkInfo;
import redstonelamp.network.pc.packet.PCDataPacket;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * EncryptionRequest 0x01 (STATE: LOGIN, S -> C)
 *
 * @author jython234
 */
public class EncryptionRequestPacket extends PCDataPacket{
    public final static int ID = PCNetworkInfo.LOGIN_ENCRYPTION_REQUEST;

    public String serverID;
    public byte[] publicKey;
    public byte[] verifyToken;

    @Override
    public int getID() {
        return PCNetworkInfo.LOGIN_ENCRYPTION_REQUEST;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putPCString(serverID);
        bb.putVarInt(publicKey.length);
        bb.put(publicKey);
        bb.putVarInt(verifyToken.length);
        bb.put(verifyToken);
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {

    }
}
