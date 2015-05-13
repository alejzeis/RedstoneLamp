package redstonelamp.utils;

public class MinecraftPacket {
	public static final int RakNetProtocolVersion = 5;
	public static final int PROTOCOL_VERSION = 24;

	// Packets Used to Join The Server
	public static final int QueryPacket = 0x01;
	public static final int ServerQueryResponse = 0x1C;
	public static final int StartLoginPacket = 0x05;
	public static final int StartLoginResponse = 0x06;
	public static final int InvalidRakNetProtocol = 0x1A;
	public static final int JoinPacket = 0x07;
	public static final int JoinResponse = 0x08;

	// Data Packets
	public static final int RakNetReliability = 0x84;

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
	public static final int FullChunkDataPacket = 0x1b; // TODO need to verify
														// the actual value to
														// be used

}
