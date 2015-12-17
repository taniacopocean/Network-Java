import java.util.HashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class HashFunction{
	
	private HashMap<String,Integer> hashMap;	//the method keeps a record of the codes for every password in a hashmap
	private int numberOfKeys = 0;
	private String plaintext = "keys.txt";
	
	public HashFunction()throws FileNotFoundException{
		hashMap = new HashMap<String,Integer>();
		Scanner sc = new Scanner(new File(plaintext));
		while(sc.hasNextLine()){
			add(sc.nextLine());
		}
		sc.close();		
	}
	
	public void add(String key){
		hashMap.put(key,++numberOfKeys);
	}
	
	public int hash(String key){	//we go through the whole hashmap, we look for the password and we return its code
		for(HashMap.Entry<String,Integer> entry: hashMap.entrySet()){
			if(entry.getKey().equals(key))
				return entry.getValue();
		}
		return -1;
	}
}
