package redstonelamp.network.pc.packet.login;

import redstonelamp.network.pc.PCNetworkInfo;
import redstonelamp.network.pc.packet.PCDataPacket;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * SetCompressionPacket 0x03 (STATE: LOGIN, S -> C)
 *
 * @author jython234
 */
public class SetCompressionPacket extends PCDataPacket{
    public final static int ID = PCNetworkInfo.LOGIN_SET_COMPRESSION;

    public int threshold;

    @Override
    public int getID() {
        return PCNetworkInfo.LOGIN_SET_COMPRESSION;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putVarInt(threshold);
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {

    }
}
