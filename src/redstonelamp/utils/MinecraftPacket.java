package redstonelamp.utils;

public class MinecraftPacket {
	public static final int RakNetProtocolVersion = 5;
	public static final int PROTOCOL_VERSION = 24;
	
	//Packets Used to Join The Server
	public static final int QueryPacket = 0x01;
	public static final int StartLoginPacket = 0x05;
		public static final int StartLoginPacketReply = 0x06;
		public static final int InvalidRakNetProtocol = 0x1A;
	public static final int JoinPacket = 0x07;
	public static final int ReadyPacket = 0x84;
}
