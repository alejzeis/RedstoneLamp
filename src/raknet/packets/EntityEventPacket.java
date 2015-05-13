package raknet.packets;
 
import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.utils.MinecraftPacket;

public class EntityEventPacket extends Packet {

	private int eid;
	private byte eventid;
	
	public EntityEventPacket(DatagramPacket p ) {
		ByteBuffer b = ByteBuffer.wrap( p.getData() );
		b.get();
		this.eid = b.getInt();
		this.eventid = b.get();
	}
	
	@Override
	public ByteBuffer getPacket() {
		ByteBuffer b = ByteBuffer.allocate(6);
		b.put((byte) MinecraftPacket.EntityEventPacket);
		b.putInt(eid);
		b.put(eventid);
		return b;
	}

	@Override
	public void process(PacketHandler h) {
		h.sendPacket(getPacket());
	}

}
