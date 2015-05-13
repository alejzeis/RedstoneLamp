package raknet.packets;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.utils.MinecraftPacket;

public class AddItemEntityPacket extends Packet {

	private int eid, type, x, y, z, speedX, speedY, speedZ;
	
	public AddItemEntityPacket(DatagramPacket p) {
		ByteBuffer b = ByteBuffer.wrap(p.getData());
		b.get();
		eid = b.getInt();
		type = b.getInt();
		x = b.getInt();
		y = b.getInt();
		z = b.getInt();
		speedX = b.getInt();
		speedY = b.getInt();
		speedZ = b.getInt();
	}
	
	@Override
	public ByteBuffer getPacket() {
		ByteBuffer b = ByteBuffer.allocate(33);
		b.put((byte) MinecraftPacket.AddItemEntityPacket);
		b.putInt(eid);
		b.putInt(type);
		b.putInt(x);
		b.putInt(y);
		b.putInt(z);
		b.putInt(speedX);
		b.putInt(speedY);
		b.putInt(speedZ);
		return null;
	}

	@Override
	public void process(PacketHandler h) {
		h.sendPacket(getPacket());
	}

}
