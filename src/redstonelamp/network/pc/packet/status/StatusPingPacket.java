package redstonelamp.network.pc.packet.status;

import redstonelamp.network.pc.PCNetworkInfo;
import redstonelamp.network.pc.packet.PCDataPacket;
import redstonelamp.utils.DynamicByteBuffer;

public class StatusPingPacket extends PCDataPacket {
	public final static int ID = PCNetworkInfo.STATUS_PING;
	
	public long id;
	
	@Override
	public int getID() {
		return PCNetworkInfo.STATUS_PING;
	}

	@Override
	protected void _encode(DynamicByteBuffer bb) {
		
	}

	@Override
	protected void _decode(DynamicByteBuffer bb) {
		this.id = bb.getLong();
	}

}
