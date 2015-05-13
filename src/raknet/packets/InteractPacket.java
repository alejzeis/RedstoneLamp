package raknet.packets;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.utils.MinecraftPacket;

public class InteractPacket extends Packet{

	private byte  action;
	private long  target;
	
	public InteractPacket(DatagramPacket p) {
		ByteBuffer b = ByteBuffer.wrap(p.getData());
		b.get();
		action = b.get();
		target = b.getLong();
	}
	
	@Override
	public ByteBuffer getPacket() {
		ByteBuffer b = ByteBuffer.allocate(6);
		b.put((byte) MinecraftPacket.InteractPacket);
		b.put(action);
		b.putLong(target);
		return b;
	}

	@Override
	public void process(PacketHandler h) {
		h.sendPacket(getPacket());
	}

}
