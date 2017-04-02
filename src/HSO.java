
/**
 * @author CodeFather
 * Child User class for HSO | UserGUI HSOInterface |
 */
public class HSO extends User{

	HSO(String username, String password, String gender, 
			 String rank,     String major,    String college) {
		super(username, password, gender, rank, major, college);
		
	}

	public void UserGUI(User user) {
		new HSOInterface();
	}

}
