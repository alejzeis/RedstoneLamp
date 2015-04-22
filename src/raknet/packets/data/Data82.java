package raknet.packets.data;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.Player;
import redstonelamp.utils.Hex;

public class Data82 extends Packet {
	private String name;
	private Player player;
	
	public Data82(DatagramPacket p, Player player) {
		ByteBuffer b = ByteBuffer.wrap(p.getData());
		b.get();
		short nameLength = b.getShort();
		name = Hex.bytesToString(b, nameLength);
		this.player = player;
		this.player.name = name;
		
	}
	
	public ByteBuffer getPacket() {
		ByteBuffer response = ByteBuffer.allocate(15);
		//		response.put((byte)0x84);
		//		response.put(Hex.intToBytes(player.getPacketCount(), 3),0,3);
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
