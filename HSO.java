import java.io.IOException;

/**
 * @author CodeFather
 * Child User class for HSO | UserGUI HSOInterface |
 */
public class HSO extends User{

	HSO(String username, String password, String gender, 
			 String rank,     String major,    String college){
		super(username, password, gender, rank, major, college);
		
	}

	
	HSO(String username, String password){
		super(username, password);
	}

	public void UserGUI(User user) {
		try {
			new HSOInterface();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
