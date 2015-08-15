package redstonelamp.network.pc.packet.handshake;

import redstonelamp.network.pc.PCNetworkInfo;
import redstonelamp.network.pc.packet.PCDataPacket;
import redstonelamp.utils.DynamicByteBuffer;

public class PongPacket extends PCDataPacket {
	public final static int ID = PCNetworkInfo.PONG;
	
	public long id = -1;
	
	@Override
	public int getID() {
		return PCNetworkInfo.PONG;
	}

	@Override
	protected void _encode(DynamicByteBuffer bb) {
		bb.putLong(id);
	}

	@Override
	protected void _decode(DynamicByteBuffer bb) {
		
	}

}
