package redstonelamp.network.pc.packet.login;

import redstonelamp.network.pc.PCNetworkInfo;
import redstonelamp.network.pc.packet.PCDataPacket;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * EncryptionResponse 0x01 (STATE: LOGIN, C -> S)
 *
 * @author jython234
 */
public class EncryptionResponse extends PCDataPacket{
    public final static int ID = PCNetworkInfo.LOGIN_ENCRYPTION_RESPONSE;

    public byte[] sharedSecret;
    public byte[] verifyToken;

    @Override
    public int getID() {
        return PCNetworkInfo.LOGIN_ENCRYPTION_RESPONSE;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {

    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {
        int len = bb.getVarInt();
        sharedSecret = bb.get(len);
        len = bb.getVarInt();
        verifyToken = bb.get(len);
    }
}
