
/**
 * @author CodeFather 
 * Child User Class for Student | UserGUI Ballot  | 
 */
public class Student extends User{
	Student(String username, String password, String gender, 
			 String rank,     String major,    String college) {
		super(username, password, gender, rank, major, college);
	}

	public void UserGUI(User user) {
		new StudentBallot(user);
		
	}

}
