import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;

public class Network{
	private static int numberOfChannels=1;
	private ArrayList<AccessPoint> accessPoints;
	private HashMap<NetworkDevices,Channel> devices;
	private ArrayList<Channel> channels;
	private HashMap<Client,AccessPoint>history;
	private Hacker elliot;
	private HashFunction hashFunction;
	
	public Network(){
		devices = new HashMap<NetworkDevices,Channel>();
		channels = new ArrayList<Channel>();
		accessPoints = new ArrayList<AccessPoint>();
		history = new HashMap<Client,AccessPoint>();
	}
	
	public void addAccessPoint(AccessPoint ap){ //adding an access point, it joins a channel
		ap.joinNetwork(this);
		Channel c = new Channel(numberOfChannels++);
		channels.add(c);
		ap.joinChannel(c);
		accessPoints.add(ap);
		
	}
	
	public void addClient(Client c){	//adding a client, it checks which access point it can connect to, and it connects
		c.joinNetwork(this);
		for(AccessPoint ap: accessPoints){
			addClientToAccessPoint(c,ap);
			if(!handshake(c,ap)){
				c.disconnect();
				System.out.println(c+" disconnects from access point");
			}
			else break;
		}
	}
	
	private void addClientToAccessPoint(Client c, AccessPoint ap){
		Channel ch = getChannel(ap);
		c.joinChannel(ch);
	}
	
	public boolean handshake(Client c, AccessPoint ap){ 	//handshake, with the 3 stages
		System.out.println("Handshaking "+c+" "+ap);
		Channel ch = getChannel(ap);
		HandshakePacket hp = c.addHandshakePacket(ch,ap,hashFunction.hash(c.getKey()));
		if(ap.getTraffic(ch)){
			ap.authoriseClient(c);
			ch.removePacket(hp);
			hp = ap.addHandshakePacket(ch,c,hashFunction.hash(ap.getKey()));				
			if(c.getTraffic(ch)){
				c.connectTo(ap);
				if(!history.containsKey(c))
					history.put(c,ap);
				System.out.println("Handshake success "+c+" connects to "+ap);
				ch.removePacket(hp);
				return true;
			}
		}
		return false;
	}
	
	public ArrayList<Channel> getChannels(){ 
			return channels;
	}
	
	public void addDeviceToChannel(NetworkDevices nd, Channel c){
		devices.put(nd,c);
	}
	
	public void removeDeviceFromChannel(NetworkDevices nd){
		devices.remove(nd);
	}
	
	public Channel getChannel(NetworkDevices nd){
		for (HashMap.Entry<NetworkDevices,Channel> entry : devices.entrySet()) {
			if(entry.getKey().equals(nd)){
				return entry.getValue();				
			}
		}
		return null;
	}
	
	public void start()throws InterruptedException,FileNotFoundException{	//the network starts
		boolean stop = false;
		while(!stop){
			for(int i = 1;i<=5;i++){						
				clearChannels();		
				networkActivity();
				if(elliot.hack(this)){
					stop = true;
					break;
				}
				Thread.sleep(100);
			}
			if(!stop)
				disconnectClients();
		}
	}
	
	private void networkActivity(){
		for(AccessPoint ap: accessPoints){
			for(Client client: ap.getAuthorisedClients()){
				if(client.isConnected()){
					client.communicate();
					ap.communicate(client);
				}
				else{
					AccessPoint lastAccessPoint = getLastAccessPoint(client);
					reconnect(client,lastAccessPoint);
				}
			}
		}
		
	}
	
	private void clearChannels(){
		for(Channel ch: channels)
			ch.clear();
	}
	
	public boolean canStart(){
		return history.size()!=0;
	}
	
	private void reconnect(Client client, AccessPoint lastAccessPoint){	//the client reconnects to the accesspoint
		Channel ch = getChannel(lastAccessPoint);
		System.out.println("Handshaking "+client+" "+lastAccessPoint);
		HandshakePacket hp = client.addHandshakePacket(ch,lastAccessPoint,hashFunction.hash(client.getKey()));
			if(lastAccessPoint.getTraffic(ch)){	
				hp = lastAccessPoint.addHandshakePacket(ch,client,hashFunction.hash(lastAccessPoint.getKey()));				
				if(client.getTraffic(ch)){
					client.connectTo(lastAccessPoint);	
					System.out.println("Handshake success "+client+" reconnects to "+lastAccessPoint);		
				}
			}
	}
	
	private AccessPoint getLastAccessPoint(Client c){
		for(HashMap.Entry<Client,AccessPoint> entry: history.entrySet()){
			if(entry.getKey().equals(c))
				return entry.getValue();
		}
		return null;
	}
	
	private void disconnectClients(){
		for(AccessPoint ap: accessPoints){
			for(Client client: ap.getAuthorisedClients())
				client.disconnectFromAccessPoint();
		}
	}
	
	public void addHacker(Hacker hacker){
		elliot = hacker;
	}
	
	public void setup()throws FileNotFoundException{
		hashFunction = new HashFunction();
	}
	
	public HashFunction getHashFunction(){
		return hashFunction;
	}
}
