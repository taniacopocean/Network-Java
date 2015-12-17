import java.io.FileNotFoundException;
public class Client extends NetworkDevices{
	
	private AccessPoint ap;	//the access point it is connected to
	
	public Client(String ad, String k)throws FileNotFoundException{		//the constructor
		super(ad,k);		
		System.out.println("Creating Client with address "+ad+". Stored key: "+k);
	}
	
	public void connectTo(AccessPoint a){	//connecting means adding the access point as the access point the client in=s connected ti
			ap = a;
	}	
		
	public void disconnect(){		//dissconnecting means removing the device from the channel. this will be done by the network
		network.removeDeviceFromChannel(this);		
	}
	
	public void communicate(){	//communicating, placing a new packet from this client to the access point into the traffic
		network.getChannel(this).placePacket(new Packet(ap.address,address));
	}

	public void disconnectFromAccessPoint(){		//disconnecting from an access point, now the access point of this client is null
		ap = null;
		System.out.println(this+" disconnects from access point");
	}
	
	public boolean isConnected(){	//checking if the client has any access point, if it is connected to any
		return ap!=null;
	}
	
	public String toString()
	{
		return "Client @"+address;
	}
}
