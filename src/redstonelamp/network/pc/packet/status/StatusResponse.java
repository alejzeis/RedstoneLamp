package redstonelamp.network.pc.packet.status;

import redstonelamp.network.pc.PCNetworkInfo;
import redstonelamp.network.pc.packet.PCDataPacket;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * Status Response packet (0x00)
 */
public class StatusResponse extends PCDataPacket {
    public static final int ID = PCNetworkInfo.STATUS_RESPONSE;

    public String jsonResponse;

    @Override
    public int getID() {
        return PCNetworkInfo.STATUS_RESPONSE;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {
        bb.putPCString(jsonResponse);
    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {

    }
}
