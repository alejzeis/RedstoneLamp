package raknet.packets.data;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.Player;
import redstonelamp.utils.Hex;

public class Data84 extends Packet {
	
	private String name;
	private Player player;
	private ArrayList<Player> players;
	
	public Data84(ArrayList<Player> players) {
		this.players = players;
	}
	
	public ByteBuffer getPacket() {
		ByteBuffer // response 
		response = ByteBuffer.allocate(15);
		
		response.put((byte) 0x60);
		response.put((byte) 0x00);
		response.put((byte) 0x28);
		response.put(Hex.intToBytes(player.getDataCount(), 3), 0, 3);
		response.put(Hex.intToBytes(1, 4), 0, 4);
		response.put((byte) 0x83);
		response.putInt(0);
		
		return response;
	}
	
	@Override
	public void process(PacketHandler h) {
		//		System.out.println("Clientname: "+ name+ " " + Hex.getHexString(getPacket().array(), true));
		h.addToQueue(getPacket());
	}
}
