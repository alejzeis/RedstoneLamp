package raknet.packets;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;


import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.utils.MinecraftPacket;


public class FullChunkDataPacket extends Packet{

	private int chunkX;
	private int chunkY;
	private String data;
	
	public FullChunkDataPacket(DatagramPacket p) {
		ByteBuffer b = ByteBuffer.wrap(p.getData());
		b.get();
		chunkX        = b.getInt();
		chunkY        = b.getInt();
		int remaining = b.remaining();
		byte [] bb    = new byte[remaining];
		data          = new String(bb);
	}
	
	@Override
	public ByteBuffer getPacket() {
		int size     = data.length();
		ByteBuffer b = ByteBuffer.allocate( 8 + size );
		b.put((byte) MinecraftPacket.FullChunkDataPacket);
		b.putInt(chunkX);
		b.putInt(chunkY);
		b.putInt(size);
		b.put(data.getBytes());
		return b;
	}

	@Override
	public void process(PacketHandler h) {
		h.sendPacket(getPacket());
	}

}
