package raknet.packets;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;

public class AddEntityPacket extends Packet {

	private int eid, type, x, y, z, speedX, speedY, speedZ, yaw, pitch;

	public AddEntityPacket(DatagramPacket p) {
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
		yaw = b.getInt();
		pitch = b.getInt();

	}

	@Override
	public ByteBuffer getPacket() {
		ByteBuffer b = ByteBuffer.allocate(41);
		b.put((byte) redstonelamp.utils.MinecraftPacket.AddEntityPacket);
		b.putInt(eid);
		b.putInt(type);
		b.putInt(x);
		b.putInt(y);
		b.putInt(z);
		b.putInt(speedX);
		b.putInt(speedY);
		b.putInt(speedZ);
		b.putInt(yaw);
		b.putInt(pitch);

		return b;
	}

	@Override
	public void process(PacketHandler h) {
		h.sendPacket(getPacket());
	}

}
