package raknet.packets.data;

import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.Player;

public class Data00 extends Packet {
	private long time;
	private Player player;
	
	public Data00(long start, Player player) {
		time = System.currentTimeMillis() - start;
		this.player = player;
	}
	
	@Override
	public ByteBuffer getPacket() {
		//		int size = 100;
		ByteBuffer response = ByteBuffer.allocate(12);
		response.put((byte) 0x00);
		response.put((byte) 0x00);
		response.put((byte) 0x48);
		response.put((byte) 0x00);
		response.putLong(time);
		
		return response;
	}
	
	@Override
	public void process(PacketHandler h) {
		//		System.out.println("Ho: " + Hex.getHexString(getPacket().array(), true));
		h.addToQueue(getPacket());
	}
}
