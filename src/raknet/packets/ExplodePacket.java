package raknet.packets;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.utils.MinecraftPacket;

public class ExplodePacket extends Packet {

	private int x,y,z;
	private float radius;

	public ExplodePacket(DatagramPacket p) {
		ByteBuffer b = ByteBuffer.wrap( p.getData() );
		b.get();
		this.x      = b.getInt();
		this.y      = b.getInt();
		this.z      = b.getInt();
		this.radius = b.getFloat(); 
	}
	
	@Override
	public ByteBuffer getPacket() {
		ByteBuffer b = ByteBuffer.allocate(17);
		b.put((byte) MinecraftPacket.ExplodePacket);
		b.putInt(x);
		b.putInt(y);
		b.putInt(z);
		b.putFloat(radius);
		return b;
	}

	@Override
	public void process(PacketHandler h) {
		h.sendPacket(getPacket());
	}
}
