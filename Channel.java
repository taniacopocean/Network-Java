import java.util.ArrayList;
public class Channel{ 
	
	private int number;		//the number of the channel
	private ArrayList<Packet> traffic;		//the traffic, memorised as a list of packets
	
	
	public Channel(int n){ 	//the constructor
		number=n;
		traffic = new ArrayList<Packet>();
	}
	
	public void placePacket(Packet p){ 		//to place the packet into the traffic, we add it to the list
		traffic.add(p);
		System.out.println("Packet added to "+ this+": "+p);
	}
	
	public void removePacket(Packet p){		//to remove the packet from the traffic, we remove it from the list
		traffic.remove(p);
	}
	
	public ArrayList<Packet> getTraffic(){ 		//getter for the traffic
		return traffic;
	}
	
	public void clear(){		//to clear the traffic, we clear the list
		traffic.clear();
	}
	
	public String toString(){		
		return "Channel "+number;
	}
	
}
