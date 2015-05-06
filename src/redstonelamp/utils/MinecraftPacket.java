package redstonelamp.utils;

public class MinecraftPacket {
	public static final int RakNetProtocolVersion = 5;
	public static final int PROTOCOL_VERSION = 25;
	
	//Packets Used to Join The Server
	public static final int QueryPacket = 0x01;
		public static final int ServerQueryResponse = 0x1C;
	public static final int StartLoginPacket = 0x05;
		public static final int StartLoginResponse = 0x06;
		public static final int InvalidRakNetProtocol = 0x1A;
	public static final int JoinPacket = 0x07;
		public static final int JoinResponse = 0x08;

	//Data Packets
	public static final int RakNetReliability = 0x84;

	//Encapsulated Packets
	public static final int MessagePacket = 0x85;
}
