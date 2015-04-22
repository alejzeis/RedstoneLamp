package redstonelamp.utils;

public class MinecraftPacket {
	public static final int PROTOCOL_VERSION = 23;
	
	//Login Packets
	public static final int ID_CONNECTED_PING_OPEN_CONNECTIONS = 0x01;
	public static final int ID_UNCONNECTED_PING_OPEN_CONNECTIONS_1 = 0x02;
	public static final int ID_OPEN_CONNECTION_REQUEST_1 = 0x05;
	public static final int ID_OPEN_CONNECTION_REPLY_1 = 0x06;
	public static final int ID_OPEN_CONNECTION_REQUEST_2 = 0x07;
	public static final int ID_OPEN_CONNECTION_REPLY_2 = 0x08;
	public static final int ID_INCOMPATIBLE_PROTOCOL_VERSION = 0x1A;
	public static final int ID_UNCONNECTED_PING_OPEN_CONNECTIONS_2 = 0x1C;
	public static final int ID_ADVERTISE_SYSTEM = 0x1D;
	public static final int CustomPacket_1 = 0x80;
	public static final int CustomPacket_2 = 0x81;
	public static final int CustomPacket_3 = 0x82;
	public static final int CustomPacket_4 = 0x83;
	public static final int CustomPacket_5 = 0x84;
	public static final int CustomPacket_6 = 0x85;
	public static final int CustomPacket_7 = 0x86;
	public static final int CustomPacket_8 = 0x87;
	public static final int CustomPacket_9 = 0x88;
	public static final int CustomPacket_10 = 0x89;
	public static final int CustomPacket_11 = 0x8A;
	public static final int CustomPacket_12 = 0x8B;
	public static final int CustomPacket_13 = 0x8C;
	public static final int CustomPacket_14 = 0x8D;
	public static final int CustomPacket_15 = 0x8E;
	public static final int CustomPacket_16 = 0x8F;
	
	//NACK/ACK Packets
	public static final int NACK = 0xA0;
	public static final int ACK = 0xC0;
	
	//Encapsulated Login Packets
	public static final int ClientConnect = 0x09;
	public static final int ServerHandshake = 0x10;
	public static final int ClientHandshake = 0x13;
	public static final int ClientCancelConnect = 0x15;
	
	//Ping/Pong Packets
	public static final int PingPacket = 0x00;
	public static final int PongPacket = 0x03;
	
	//DATA Packets
	public static final int LoginPacket = 0x82;
	public static final int LoginStatusPacket = 0x83;
	public static final int ReadyPacket = 0x84;
	public static final int MessagePacket = 0x85;
	public static final int SetTimePacket = 0x86;
	public static final int StartGamePacket = 0x87;
	public static final int AddMobPacket = 0x88;
	public static final int AddPlayerPacket = 0x89;
	public static final int RemovePlayerPacket = 0x8a;
	public static final int AddEntityPacket = 0x8c;
	public static final int RemoveEntityPacket = 0x8d;
	public static final int AddItemEntityPacket = 0x8e;
	public static final int TakeItemEntityPacket = 0x8f;
	public static final int MoveEntityPacket = 0x90;
	public static final int MoveEntityPacket_PosRot = 0x93;
	public static final int MovePlayerPacket = 0x94;
	public static final int PlaceBlockPacket = 0x95;
	public static final int RemoveBlockPacket = 0x96;
	public static final int UpdateBlockPacket = 0x97;
	public static final int AddPaintingPacket = 0x98;
	public static final int ExplodePacket = 0x99;
	public static final int LevelEventPacket = 0x9a;
	public static final int TileEventPacket = 0x9b;
	public static final int EntityEventPacket = 0x9c;
	public static final int RequestChunkPacket = 0x9d;
	public static final int ChunkDataPacket = 0x9e;
	public static final int PlayerEquipmentPacket = 0x9f;
	public static final int PlayerArmorEquipmentPacket = 0xa0;
	public static final int InteractPacket = 0xa1;
	public static final int UseItemPacket = 0xa2;
	public static final int PlayerActionPacket = 0xa3;
	public static final int HurtArmorPacket = 0xa5;
	public static final int SetEntityDataPacket = 0xa6;
	public static final int SetEntityMotionPacket = 0xa7;
	public static final int SetRidingPacket = 0xa8;
	public static final int SetHealthPacket = 0xa9;
	public static final int SetSpawnPositionPacket = 0xaa;
	public static final int AnimatePacket = 0xab;
	public static final int RespawnPacket = 0xac;
	public static final int SendInventoryPacket = 0xad;
	public static final int DropItemPacket = 0xae;
	public static final int ContainerOpenPacket = 0xaf;
	public static final int ContainerClosePacket = 0xb0;
	public static final int ContainerSetSlotPacket = 0xb1;
	public static final int ContainerSetDataPacket = 0xb2;
	public static final int ContainerSetContentPacket = 0xb3;
	public static final int ContainerAckPacket = 0xb4;
	public static final int ChatPacket = 0xb5;
	public static final int SignUpdatePacket = 0xb6;
	public static final int AdventureSettingsPacket = 0xb7;
}
