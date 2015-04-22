package raknet.packets.data;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.utils.Hex;

public class RequestChunkPacket extends Packet {
	private int x, z;
	
	public RequestChunkPacket(DatagramPacket p) {
		ByteBuffer b = ByteBuffer.wrap(p.getData());
		b.get();
		x = b.getInt();
		z = b.getInt();
		
	}
	
	@Override
	public ByteBuffer getPacket() {
		int size = 265 * 8;
		ByteBuffer response = ByteBuffer.allocate(3 + 265);
		response.put((byte) 0x00);
		response.putShort((short) size);
		response.put((byte) 0x9e);
		response.putInt(x);
		response.putInt(z);
		//	for (int i = 0; i < 256; i++) {
		//	    int rndBlock = 1 + (int) (Math.random() * 256);
		response.put(Hex.intToBytes(0, 256));
		//	}
		return response;
	}
	
	@Override
	public void process(PacketHandler h) {
		h.addToQueue(getPacket());
	}
}
