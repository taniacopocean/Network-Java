import java.util.ArrayList;
import java.io.FileNotFoundException;
public class AccessPoint extends NetworkDevices{ //A class AccessPoint whici extends NetworkDevices, because an access point is a network device
	
	private ArrayList<Client> authorisedClients; //A list with the autorised clients
	
	public AccessPoint(String ad, String k) throws FileNotFoundException{	//The constructor of the class
		super(ad,k);
		authorisedClients = new ArrayList<Client>();
		System.out.println("Creating AccessPoint with address "+ad+". Stored key: "+k);

	}
	
	public void authoriseClient(Client client){	 //A method to add an authorised client
		if(!authorisedClients.contains(client))	 //If the list doesn't already contain the client, it will be added to the list
			authorisedClients.add(client);
	}
	
	public void communicate(Client c){		//The method which supports communication between devices in a network activity
		Channel ch = network.getChannel(this);		//Finind out what channel this access point belongs to
		ArrayList<Packet> list = new ArrayList<Packet>(); 	//Copying the traffic list, so I don't get a concurrent modification exception
		for(Packet pack: ch.getTraffic())
			list.add(pack);
		for(Packet p: list){		//We go through the traffic and check if the destination address on the packet matches the access point's address
			if(p.getDestinationAddress().equals(address)){
				ch.placePacket(new Packet(c.address,address)); //If so, a new packet from this access point to the client is placet into the traffic
			}
		}
	}
	
	public ArrayList<Client> getAuthorisedClients(){	//A method to get the list of authorised clients
		return authorisedClients;
	}
	
	public String toString(){		//A toString method, to return the address of an access point
		return "AccessPoint @"+address;
	}
}
