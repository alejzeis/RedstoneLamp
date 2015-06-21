Although there have not been any commits lately, we are NOT dead!  We are working on a large-scale commit in the background that will fix a lot of issues we were having in development!

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
		- [x] Ping Packets
			- [x] 0x02 - ID_UNCONNECTED_PING_OPEN_CONNECTIONS_1 => 0x1C - ID_UNCONNECTED_PONG
		- [ ] Connection Packets
			- [x] 0x05 - ID_OPEN_CONNECTION_REQUEST_1 => 0x06 - ID_OPEN_CONNECTION_REPLY_1
			- [x] 0x07 - ID_OPEN_CONNECTION_REQUEST_2 => 0x08 - ID_OPEN_CONNECTION_REPLY_2
			- [ ] 0x09 - ClientConnect => 0x10 - ServerHandshake
			- [ ] 0x13 - ClientHandshake
			- [ ] 0x82 - Login => 0x83 - LoginStatus => 0x87 - StartGame => 0x86 - SetTime => (Send Chunks)
			- [ ] 0x84 - Ready
		
	- [ ] Send Packets
		- [ ] Chunk Sending
		- [ ] Other Needed Packets
	- [x] Accept Packets
	- [x] Correct Protocol Number
	
- [ ] Implement Items
- [ ] Implement Blocks
- [ ] Level Generation
- [x] Plugin Loading
- [x] Command Registration
- [x] Command Handling
- [ ] Multi-language support

**Please read the CONTRIBUTING.md before making Pull Requests**
