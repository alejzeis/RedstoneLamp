package raknet.packets.data;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;

public class RemoveBlockPacket extends Packet {
	private int entityID;
	private int x, z;
	private byte y;
	
	public RemoveBlockPacket(DatagramPacket p) {
		ByteBuffer b = ByteBuffer.wrap(p.getData());
		b.get();
		entityID = b.getInt();
		x = b.getInt();
		z = b.getInt();
		y = b.get();
	}
	
	@Override
	public ByteBuffer getPacket() {
		int size = 12 * 8;
		ByteBuffer response = ByteBuffer.allocate(3 + 12);
		response.put((byte) 0x00);
		response.putShort((short) size);
		
		response.put((byte) 0x97);
		response.putInt(x);
		response.putInt(z);
		response.put(y);
		response.put((byte) 0x00);
		response.put((byte) 0x00);
		return response;
	}
	
	@Override
	public void process(PacketHandler h) {
		h.addToQueueForAll(getPacket());
	}
}
