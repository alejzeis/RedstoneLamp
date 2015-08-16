package redstonelamp.network.pc.packet.status;

import redstonelamp.network.pc.PCNetworkInfo;
import redstonelamp.network.pc.packet.PCDataPacket;
import redstonelamp.utils.DynamicByteBuffer;

public class StatusPongPacket extends PCDataPacket {
	public final static int ID = PCNetworkInfo.STATUS_PONG;
	
	public long id;
	
	@Override
	public int getID() {
		return PCNetworkInfo.STATUS_PONG;
	}

	@Override
	protected void _encode(DynamicByteBuffer bb) {
		bb.putLong(id);
	}

	@Override
	protected void _decode(DynamicByteBuffer bb) {
		
	}

}
