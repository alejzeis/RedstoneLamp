package raknet;

import java.nio.ByteBuffer;

public abstract class Packet {
	public abstract ByteBuffer getPacket();
	
	public abstract void process(PacketHandler h);
}
