package raknet.packets;

import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;

public class ReadyPacket extends Packet {
	public ReadyPacket() {
		
	}
	
	@Override
	public ByteBuffer getPacket() {
		return null;
	}
	
	@Override
	public void process(PacketHandler h) {
		
	}
}
