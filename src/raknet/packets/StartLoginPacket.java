package raknet.packets;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.RedstoneLamp;
import redstonelamp.utils.MinecraftPacket;

public class StartLoginPacket extends Packet {
	private byte[] magic = new byte[16];
	private short mtuSize;
	private long serverID;
	private int protocol;
	private InetAddress clientAddress;
	private int clientPort;
	
	public StartLoginPacket(DatagramPacket p, long serverID, InetAddress clientAddress, int clientPort) {
		ByteBuffer b = ByteBuffer.wrap(p.getData());
		if(!(b.get() == MinecraftPacket.StartLoginPacket))
			return;
		b.get(magic);
		this.protocol = b.get();
		this.mtuSize = (short) p.getLength();
		this.serverID = serverID;
		this.clientAddress = clientAddress;
		this.clientPort = clientPort;
	}
	
	@Override
	public ByteBuffer getPacket() {
		ByteBuffer response;
		if(protocol == MinecraftPacket.RakNetProtocolVersion) {
			response = ByteBuffer.allocate(28);
			response.put((byte) MinecraftPacket.StartLoginPacketReply);
			response.put(magic);
			response.putLong(serverID);
			response.put((byte) 0x00);
			response.putShort(mtuSize);
		} else {
			response = ByteBuffer.allocate(26);
			response.put((byte) MinecraftPacket.InvalidRakNetProtocol);
			response.put((byte) MinecraftPacket.RakNetProtocolVersion);
			response.put(magic);
			response.putLong(serverID);
			RedstoneLamp.server.getLogger().info(this.clientAddress + ":" + this.clientPort + " has logged out due to incorrect RakNet protocol version");
		}
		
		return response;
	}

	@Override
	public void process(PacketHandler h) {
		h.sendPacket(getPacket());
	}
}
