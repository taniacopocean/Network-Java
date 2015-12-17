# Network-Java
README file

Network is a project that contains a network and some devices that can connect to it (Clients and AccessPoints). 
These communicate through Channels, by sending Packets. 
When trying to connect, the Client sends a HandshakePacket (a specific kind of packet) to the AccessPoint, which checks if the key is correct.
Then, the network activity begins, but 10% of the cases, the Client disconnects, making in possible for a hacker to get into the network and get the key. Then, he can connect its own device(client) to the network. 
In order to encript the keys, a HashFuction was used.
