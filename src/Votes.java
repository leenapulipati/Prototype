import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Votes implements java.io.Serializable {
	 HashMap<String, User> votedUsers;			/**UserName -> User**/
	 HashMap<String, String[]> IDStatistics;    /**VoterID -> Statistics**/   
	 HashMap<String, List<Candidate>> voterIDs; /**VoterIDs -> Candidates**/ 
	 HashMap<String, List<Candidate>> votes;	/**Race -> Candidates**/
	 HashMap<String, Integer> summaryStatistcs; /**UserName -> SummaryStatistics**/
	ArrayList <Boolean> raceVoteSelection = new ArrayList<>(); /**Selection Checks**/
	
	 public Votes() 
	 {
		votedUsers = new  HashMap<String, User>();
		votes = new HashMap<String, List<Candidate>>();
		voterIDs = new HashMap<String, List<Candidate>>();
		summaryStatistcs= new HashMap<String, Integer>();
		IDStatistics = new HashMap<String, String[]>();
	}
	
}
