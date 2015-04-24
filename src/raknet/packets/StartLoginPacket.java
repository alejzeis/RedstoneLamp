package raknet.packets;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import raknet.MinecraftPacket;
import raknet.Packet;
import raknet.PacketHandler;
import redstonelamp.RedstoneLamp;

public class StartLoginPacket extends Packet {
	private byte[] magic = new byte[16];
	private short mtuSize;
	private long serverID;
	private int protocol;
	private InetAddress clientAddress;
	private int clientPort;
	
	public StartLoginPacket(DatagramPacket p, long serverID, InetAddress clientAddress, int clientPort) {
		ByteBuffer b = ByteBuffer.wrap(p.getData());
		if(!(b.get() == MinecraftPacket.ID_OPEN_CONNECTION_REQUEST_1))
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
		ByteBuffer response = ByteBuffer.allocate(28);
		if(protocol == MinecraftPacket.RAKNET_PROTOCOL_VERSION) {
			response.put((byte) MinecraftPacket.ID_OPEN_CONNECTION_REPLY_1);
			response.put((byte) 0x00);
			response.putShort(mtuSize);
		} else {
			response.put((byte) MinecraftPacket.ID_INCOMPATIBLE_PROTOCOL_VERSION);
			RedstoneLamp.server.getLogger().info(this.clientAddress + ":" + this.clientPort + " has logged out due to incorrect RakNet protocol version");
		}
		response.put(magic);
		response.putLong(serverID);
		
		return response;
	}

	@Override
	public void process(PacketHandler h) {
		h.sendPacket(getPacket());
	}
}
