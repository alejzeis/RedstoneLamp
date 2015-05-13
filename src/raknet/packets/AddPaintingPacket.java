package raknet.packets;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;


import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.utils.Hex;
import redstonelamp.utils.MinecraftPacket;

public class AddPaintingPacket extends Packet {

	private int eid, x, y, z, direction;
	private String title;
	
	public AddPaintingPacket(DatagramPacket p) {
		ByteBuffer b = ByteBuffer.wrap(p.getData());
		eid  = b.getInt();
		x    = b.getInt();
		y    = b.getInt();
		z    = b.getInt();
		direction = b.getInt();
		int size = b.remaining();
		title = Hex.bytesToString(b, (short)size);
	}
	
	@Override
	public ByteBuffer getPacket() {
		int size     = title.length();
		ByteBuffer b = ByteBuffer.allocate(21 + size);
		b.put((byte) MinecraftPacket.AddPaintingPacket);
		b.putInt(eid);
		b.putInt(x);
		b.putInt(y);
		b.putInt(z);
		b.putInt(direction);
		b.put(title.getBytes());
		return b;
	}

	@Override
	public void process(PacketHandler h) {
		h.sendPacket(getPacket());
	}

}