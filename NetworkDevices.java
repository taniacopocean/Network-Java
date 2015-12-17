import java.util.ArrayList;
import java.io.FileNotFoundException;
public class NetworkDevices{
	
	protected String address;
	protected String key;
	protected Network network;
	private HashFunction hashFunction = new HashFunction();
	
	public NetworkDevices(String ad, String k) throws FileNotFoundException{
		address=ad;		
		key=k;
	}
	
	public void joinNetwork(Network n){
		network = n;
	}
	public String getAddress(){
		return address;
	}
	
	public String getKey(){
		return key;	
	}
	
	public void joinChannel(Channel c){
		network.addDeviceToChannel(this,c);
	}	
	
	public HandshakePacket addHandshakePacket(Channel c, NetworkDevices nd, int key){
		HandshakePacket hp = new HandshakePacket(nd.address,address,key);
		c.placePacket(hp);
		return hp;
	}
	
	public boolean getTraffic(Channel c){
		
		for(Packet p: c.getTraffic()){
			if(p.isHandshakePacket() && p.getDestinationAddress().equals(address)){
				if(((HandshakePacket)p).getKey()==hashFunction.hash(key)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean equals( Object object ) {
		
		NetworkDevices device = (NetworkDevices)object;
		
		return device.address.equals(address);
	
	}
}
