# RedstoneLamp	[![Build Status](https://travis-ci.org/RedstoneLamp/RedstoneLamp.svg?branch=master)](https://travis-ci.org/RedstoneLamp/RedstoneLamp)
MinecraftPE Server Software


#Links
RedstoneLamp - http://RedstoneLamp.net

RedstoneLamp Forums - http://forums.RedstoneLamp.net

RedstoneLamp Documentation - http://docs.RedstoneLamp.net

Get Plugins - http://forums.redstonelamp.net/index.php?resources/

# TODO
- [ ] Working Packet Sending/Handling

	- [x] Handle Login Packets
		- [x] 0x02 - ID_UNCONNECTED_PING_OPEN_CONNECTIONS_1
		- [x] 0x05 - ID_OPEN_CONNECTION_REQUEST_1
		- [x] 0x07 - ID_OPEN_CONNECTION_REPLY_2
	- [ ] Handle Data Packets
		- [ ] 0x84 - RakNet Reliability Packet
		- [ ] Other Packets
		
	- [ ] Send Packets
		- [ ] Chunk Sending
		- [ ] Other Needed Packets
	- [x] Accept Packets
	- [x] Correct Protocol Number
	
- [ ] Implement Items
- [ ] Implement Blocks
- [ ] Level Generation
- [ ] Plugin Loading
- [x] Command Registration
- [ ] Command Handling
- [ ] More language support

**Please read the contributing.md before making Pull Requests**
