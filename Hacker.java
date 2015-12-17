import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Hacker{
	
	private Client targetClient;
	private AccessPoint targetAccessPoint;
	private boolean halfHandshake = false; //a hacker can have only a half of a handshake
	private String key;
	private HashFunction hashFunction; //a hacker can access a hashfunction
	private String plaintext = "keys.txt"; //the file where the hacker can read the passwords from
	
	public Hacker(Client c, AccessPoint ap)throws FileNotFoundException{	//constructor
		targetClient = c;
		targetAccessPoint = ap;
		hashFunction = new HashFunction();
	}
	
	public void joinNetwork(Network network){	//join a network, the network adds this as its hacker
		network.addHacker(this);
	}
	
	public boolean hack(Network n)throws FileNotFoundException{ 		//the hack method
		
		for(Channel ch: n.getChannels()){	//the hacker goes through all the channels
			for(Packet p: ch.getTraffic()){	//and gets their traffic
				if(p.isHandshakePacket()&&isTarget(p)){	//if he finds a handshake packet in target he has half a handshake
					if(halfHandshake == true){	//if he previously had half a handshake, he can start hacking
						System.out.println("Hacker captures handshake!");
						extractKey((HandshakePacket)p);	//he extracts the key
						System.out.println("Cracked handshake, password for "+targetAccessPoint+" is "+key);
						n.handshake(new Client("adrh",key),targetAccessPoint);		//and handshakes his own client
						return true;
					}					
					else
						halfHandshake = true;				//if not, he has half a handshake	
				}
			}
		}
		return false;
	}
	
	private boolean isTarget(Packet p){
		return (p.getDestinationAddress().equals(targetAccessPoint.getAddress())&&p.getSourceAddress().equals(targetClient.getAddress()))||(p.getDestinationAddress().equals(targetClient.getAddress())&&p.getSourceAddress().equals(targetAccessPoint.getAddress()));
	}
	
	private void extractKey(HandshakePacket p)throws FileNotFoundException{
		key = transformKey(p.getKey());		
	}
	
	private String transformKey(int k) throws FileNotFoundException{ //the key has to be transformed by a hashfunction
		Scanner sc = new Scanner(new File(plaintext));
		while(sc.hasNextLine()){
			String key = sc.nextLine();
			if(hashFunction.hash(key)==k)
				return key;
		}
		sc.close();
		return null;
	}

}
