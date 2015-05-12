package raknet.packets;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.Player;

public class MovePlayerPacket extends Packet{

	private int entityID,mode;
	private float x, y, z, yaw, pitch, bodyYaw;
	private Player player;
	
	public MovePlayerPacket(DatagramPacket p, Player player) {
		ByteBuffer b = ByteBuffer.wrap(p.getData());
		b.get();
		entityID = b.getInt();
		x = b.getFloat();
		y = b.getFloat();
		z = b.getFloat();
		yaw = b.getFloat();
		bodyYaw = b.getFloat();
		pitch = b.getFloat();
		mode = b.get();
		this.player = player;
	}


	@Override
	public void process(PacketHandler h) {
		h.addToQueue(getPacket());
		player.x = x;
		player.y = y;
		player.z = z;
		player.yaw = yaw;
		player.pitch = pitch;
	}


	@Override
	public ByteBuffer getPacket() {
		int size = 25 * 8;
		ByteBuffer response = ByteBuffer.allocate(3 + 25);
		response.put((byte) 0x00);
		response.putShort((short) size);
		response.put((byte) 0x94);
		response.putInt(entityID);
		response.putFloat(x);
		response.putFloat(y);
		response.putFloat(z);
		response.putFloat(yaw);
		response.putFloat(bodyYaw);
		response.putFloat(pitch);
		response.putFloat(mode);
		return response;
	}

}
