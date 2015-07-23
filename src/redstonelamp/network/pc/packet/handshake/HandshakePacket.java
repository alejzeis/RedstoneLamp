package redstonelamp.network.pc.packet.handshake;

import redstonelamp.network.packet.DataPacket;
import redstonelamp.network.pc.PCNetworkInfo;
import redstonelamp.utils.DynamicByteBuffer;

/**
 * Created by jython234 on 7/23/2015.
 */
public class HandshakePacket extends DataPacket{
    public final static byte ID = PCNetworkInfo.HANDHSAKE_HANDSHAKE;

    public int protocolVersion;
    public String serverAddress;
    public int serverPort;
    public int nextState;

    @Override
    public byte getPID() {
        return PCNetworkInfo.HANDHSAKE_HANDSHAKE;
    }

    @Override
    protected void _encode(DynamicByteBuffer bb) {

    }

    @Override
    protected void _decode(DynamicByteBuffer bb) {

    }
}
