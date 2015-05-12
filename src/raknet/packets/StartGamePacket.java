package raknet.packets;

import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.Player;
import redstonelamp.utils.Hex;

public class StartGamePacket extends Packet{
	
private Player player;
	
	public StartGamePacket(Player player) {
		this.player = player;
	}
	
	@Override
	public ByteBuffer getPacket() {
		ByteBuffer response = ByteBuffer.allocate(39);
		response.put((byte) 0x60);
		response.put((byte) 0x00);
		response.put((byte) 0x05);
		response.put(Hex.intToBytes(player.getDataCount(), 3), 0, 3); 
		response.put(Hex.intToBytes(2, 4));
		response.put((byte) 0x87);
		response.putInt(1441750673); 
		response.putInt(0);
		response.putInt(1); 
		response.putInt(player.entityID); 
		response.putFloat(128.0f); // X
		response.putFloat(128.0f); // Y
		response.putFloat(128.0f); // Z
		return response;
	}

	@Override
	public void process(PacketHandler h) {
		h.addToQueue(getPacket());
		player.x = 128.0f;
		player.y = 128.0f;
		player.z = 128.0f;
	}
	
}
