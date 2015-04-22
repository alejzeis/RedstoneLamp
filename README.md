# RedstoneLamp	[![Build Status](https://travis-ci.org/RedstoneLamp/RedstoneLamp.svg?branch=master)](https://travis-ci.org/RedstoneLamp/RedstoneLamp)
MinecraftPE Server Software


#Links
RedstoneLamp - http://RedstoneLamp.net

RedstoneLamp Forums - http://forums.RedstoneLamp.net

RedstoneLamo Documentation - http://docs.RedstoneLamp.net


# TODO
- [ ] Working Packet Sending/Handling

	- [x] Handle Login Packets
		- [x] 0x02 - ID_UNCONNECTED_PING_OPEN_CONNECTIONS_1
		- [x] 0x05 - ID_OPEN_CONNECTION_REQUEST_1
		- [x] 0x07 - ID_OPEN_CONNECTION_REPLY_2
	- [x] Handle Data Packets
		- [x] 0x84 - CustomPacket #5
		- [x] 0x85 - CustomPacket #6
		- [x] 0x86 - CustomPacket #7
		- [x] 0x87 - CustomPacket #8
		- [x] 0x88 - CustomPacket #9
		- [x] 0x89 - CustomPacket #10
		- [x] 0x8A - CustomPacket #11
		- [x] 0x8B - CustomPacket #12
		- [x] 0x8C - CustomPacket #13
		- [x] 0x8D - CustomPacket #14
		- [x] 0x8E - CustomPacket #15
		- [x] 0x8F - CustomPacket #16
		
	-[ ] Send Packets
		- [ ]Chunk Sending
		- [ ] Other Needed Packets
	- [ ] Accept Packets
	- [ ] Correct Protocol Number
	
- [ ] Implement Items
- [ ] Implement Blocks
- [ ] Level Generation
- [ ] Plugin Loading
- [ ] Command Registration
- [ ] Command Handling