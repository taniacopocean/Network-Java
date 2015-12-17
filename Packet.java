public class Packet{
	
	protected String destinationAddress;
	protected String sourceAddress;

	public Packet(String da, String sa){
		destinationAddress = da;
		sourceAddress = sa;
	}
	
	public String getDestinationAddress(){
		return destinationAddress;
	}
	
	public String getSourceAddress(){
		return sourceAddress;
	}
	
	public String toString(){
		String n;
		if(isHandshakePacket())
			n="HandshakePacket ";
		else
			n="Packet ";
		return n+"(Source: "+sourceAddress+" Destination: "+destinationAddress+")";
	}
	
	public boolean isHandshakePacket(){
		return this instanceof HandshakePacket;
	}
}
