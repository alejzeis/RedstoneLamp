package raknet.packets;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.Player;

public class PlayerActionPacket extends Packet {

	private Player player;
	private int eID,action,x,y,z,face;
	
	public PlayerActionPacket(DatagramPacket p, Player player) {
		ByteBuffer b = ByteBuffer.wrap(p.getData());
		eID     = b.getInt();
		action  = b.getInt();
		x       = b.getInt();
		y       = b.getInt();
		z       = b.getInt();
		face    = b.getInt();
		this.player = player;
	}
	
	@Override
	public ByteBuffer getPacket() {
		ByteBuffer b = ByteBuffer.allocate(20);
		b.put((byte) 0xa3);
		b.putInt(eID);
		b.putInt(action);
		b.putInt(x);
		b.putInt(y);
		b.putInt(z);
		b.putInt(face);
		return b;
	}

	@Override
	public void process(PacketHandler h) {
		h.addToQueue(getPacket());
		player.x = x;
		player.y = y;
		player.z = z;
		player.face = face;
	}

}
