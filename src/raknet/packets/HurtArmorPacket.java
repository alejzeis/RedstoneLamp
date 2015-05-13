package raknet.packets;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.utils.MinecraftPacket;

public class HurtArmorPacket extends Packet {

	private byte health;
	
	public HurtArmorPacket(DatagramPacket p) {
		ByteBuffer b = ByteBuffer.wrap(p.getData());
		b.get();
		health = b.get();
	}
	
	@Override
	public ByteBuffer getPacket() {
		ByteBuffer b = ByteBuffer.allocate(2);
		b.put((byte)MinecraftPacket.HurtArmorPacket);
		b.put(health);
		return b;
	}

	@Override
	public void process(PacketHandler h) {
		h.sendPacket(getPacket());
	}
}
