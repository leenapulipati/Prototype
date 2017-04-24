import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * @author CodeFather 
 * Abstract Parent User Class
 * Each user has a | Username | Password | UserGUI |
 */
public abstract class User implements java.io.Serializable{

	String username;
	String password;	
	String gender;
	String rank;
	String major;
	String college;
	String electionName;
		
	User(String username, String password, String gender, 
		 String rank,     String major,    String college){
		
		this.username = username;
		this.password = password;
		this.gender = gender;
		this.rank = rank;
		this.major = major;
		this.college = college;
	}

	User(String username, String password, String electionName){
			
			this.username = username;
			this.password = password;
			this.electionName = electionName;

		}
	public String[] dataSet(){
		String[] data = new String[4];
		data[0] = gender;
		data[1] = rank;
		data[2] = major;
		data[3] = college;
		return data;
	}
	
	/**Each GUI takes in a user**/
	public abstract void UserGUI(User user);

	public String toString(){
		String result =   gender   + " " + rank  + " " +
						   major    + " " +
						   college  ;
		return result;
	}
	public static void main(String[]args){
		
	}


}
