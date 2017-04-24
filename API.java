import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @author CodeFather
 * API must be run in order to fill the user hashmap
 * 
 * NOTE >>> API creates a serialized file containing the hashmap 
 * 			when the server class is run the file is deserialized 
 * 			in order to initialize the user hashmap
 * 
 *		>>> API only needs to be run once in order to create the initial serialized file
 *			Afterwords only deserialization from the server class is necessary 
 *			at the server run
 */
public class API {
	@SuppressWarnings("resource")
	/**
	 * API Constructor 
	 * fills MyVoteServer with users
	 * 	| Students | Election Commissioners | HSO | OIT |
	 */
	API(){
		try{
		Scanner apiReader = new Scanner(new File("API.txt"));
		while(apiReader.hasNext()){
			
			String[] user = apiReader.nextLine().split(" ");
			
			
			if(user[0].equals("EC"))
				MyVoteServer.users.put(user[1], new ElectionCommissioner(user[1], user[2],user[3]));
			else if(user[0].equals("HSO"))
				MyVoteServer.users.put(user[1], new HSO(user[1], user[2]));
			else if(user.length < 7){
				MyVoteServer.users.put(user[0], new Student(user[0], user[1],user[2],user[3],user[4],user[5]));
				
			}
		}
		
		}catch(IOException e){
			System.out.println("API not present...");
		}
	}
	
	/**
	 *  serializeAPI method
	 *  creates a simplified file representing the API User Hashmap 
	 *  >>>>filename = MyVoteAPI.ser
	 * TODO Modify serilization so it doesn't have to work for static user hashmap
	 */
	public static void serializeAPI(){
		
		try{
			
			ObjectOutputStream apiOut = new ObjectOutputStream(new FileOutputStream("MyVoteAPI.ser"));
			apiOut.writeObject(MyVoteServer.users);
			apiOut.close();
			System.out.println("Serialization Complete!");

		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 *  deserializeAPI method
	 *  reads the serialized file >>> MyVoteAPI.ser 
	 *  and "deserializes" (re-forms into User Hashmap) file to initialize
	 *  user hashmap
	 *  
	 * TODO Modify deserilization so it doesn't have to work for static user hashmap
	 */
	@SuppressWarnings("unchecked")
	public static void deserializeAPI(){
		
		try{
			
			ObjectInputStream apiIn = new ObjectInputStream(new FileInputStream("MyVoteAPI.ser"));
			MyVoteServer.users = (HashMap<String,User>) apiIn.readObject();
			
			System.out.println("Deserialization Complete!");
			apiIn.close();
			
			
		}catch(IOException e){
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("API Not Found...");
			e.printStackTrace();
		}
		
	}
	public static void main (String[]args){
		new MyVoteServer(false).start();
		new API();
		serializeAPI();
	}
}
