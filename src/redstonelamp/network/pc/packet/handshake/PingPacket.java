package redstonelamp.network.pc.packet.handshake;

import redstonelamp.network.pc.PCNetworkInfo;
import redstonelamp.network.pc.packet.PCDataPacket;
import redstonelamp.utils.DynamicByteBuffer;

public class PingPacket extends PCDataPacket {
	public final static int ID = PCNetworkInfo.PING;
	
	public long id = -1;
	
	@Override
	public int getID() {
		return PCNetworkInfo.PING;
	}

	@Override
	protected void _encode(DynamicByteBuffer bb) {
		
	}

	@Override
	protected void _decode(DynamicByteBuffer bb) {
		this.id = bb.getLong();
	}

}
