public class HandshakePacket extends Packet{

	private int key;
	
	public HandshakePacket(String da,String sa,int k){
		super(da,sa);
		key=k;			
	}
	
	public int getKey(){
		return key;	
	}
	
	public String toString(){
		return "HandshakePacket (Source: "+sourceAddress+" Destination: "+destinationAddress+")";
	}
}
