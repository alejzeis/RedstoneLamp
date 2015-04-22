package raknet.query;

import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;

public class QueryPacket extends Packet {
	public QueryPacket() {
		
	}
	
	@Override
	public ByteBuffer getPacket() {
		return null;
	}
	
	@Override
	public void process(PacketHandler h) {
		
	}
}
