package raknet.packets.login;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.Player;
import redstonelamp.RedstoneLamp;

public class Login07 extends Packet {
	private byte packetType;
	private byte[] magic = new byte[16];
	private byte[] cookie = new byte[4];
	private short mtuSize;
	private RedstoneLamp redstone;
	private short clientPort;
	private long clientID;
	private long serverID;
	private Player player;
	private RedstoneLamp server;
	private DatagramPacket packet;
	
	public Login07(DatagramPacket p, long serverID, RedstoneLamp server) {
		ByteBuffer b = ByteBuffer.wrap(p.getData());
		packetType = b.get();
		if(packetType != 0x07) { return; }
		
		b.get(magic);
		b.get(cookie);
		b.get();
		mtuSize = b.getShort();
		clientID = b.getLong();
		clientPort = (short) p.getPort();
		
		this.serverID = serverID;
		this.server = server;
		packet = p;
	}
	
	public ByteBuffer getPacket() {
		int size = 35;
		ByteBuffer response = ByteBuffer.allocate(size);
		
		response.put((byte) 0x08);
		response.put(magic);
		response.putLong(serverID);
		response.put(cookie);
		response.put((byte) 0xcd);
		response.putShort(clientPort);
		response.putShort((short) mtuSize);
		response.put((byte) 0x00);
		return response;
	}
	
	@Override
	public void process(PacketHandler h) {
		server.addPlayer(packet.getAddress(), packet.getPort(), clientID);
		//		player.setClientID(clientID);
		h.sendPacket(getPacket());
	}
}
