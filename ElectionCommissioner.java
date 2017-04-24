
/**
 * @author CodeFather 
 * Child User class for Election Commissioner 
 * User GUI | BallotPrompt |
 */
public class ElectionCommissioner extends User  implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	ElectionCommissioner(String username, String password, String gender, 
			 String rank,     String major,    String college) {
		super(username, password, gender, rank, major, college);

	}
	
	ElectionCommissioner(String username, String password, String electionName){
		super(username, password, electionName);

	}
	
	public void UserGUI(User user) {
		new BallotPrompt();
	}

}
