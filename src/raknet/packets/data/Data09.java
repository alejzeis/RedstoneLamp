package raknet.packets.data;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.Player;
import redstonelamp.utils.Hex;

public class Data09 extends Packet {
	private long clientID;
	private byte[] magic = new byte[16];
	private long serverID;
	private long unknown1;
	private int clientPort;
	private Player player;
	
	public Data09(DatagramPacket p, Player player, long serverID) {
		ByteBuffer b = ByteBuffer.wrap(p.getData());
		b.get();
		b.getLong();
		unknown1 = b.getLong();
		this.player = player;
		this.serverID = serverID;
	}
	
	@Override
	public ByteBuffer getPacket() {
		//		int size = 100;
		ByteBuffer response = ByteBuffer.allocate(106);
		//		response.put((byte)0x84);
		//		response.put(Hex.intToBytes(player.getPacketCount(), 3),0,3);
		response.put((byte) 0x60); // Encapsulation ID
		response.put((byte) 0x03);
		response.put((byte) 0x00); // size of packet
		response.put(Hex.intToBytes(player.getDataCount(), 3), 0, 3);
		response.put((byte) 0x00); // MinecrafPE ID
		response.putInt(16);
		response.put((byte) 0x04);
		response.put((byte) 0x3f);
		response.put((byte) 0x57);
		response.put((byte) 0xfe);
		response.put((byte) 0xcd);
		response.putShort((short) player.clientPort);
		for(int i = 0; i < 10; i++) {
			response.put(Hex.intToBytes(4, 3));
			response.putInt(0xffffffff);
		}
		response.putShort((short) 0);
		response.putLong(unknown1);
		response.putLong(1L);
		
		return response;
	}
	
	@Override
	public void process(PacketHandler h) {
		//		System.out.println("Ho: " + Hex.getHexString(getPacket().array(), true));
		h.addToQueue(getPacket());
	}
}
