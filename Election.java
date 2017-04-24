import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Election  implements java.io.Serializable{

	private static final long serialVersionUID = 1L;

	String election_title;
	 
	 HashMap<String, User> votedUsers;			/**UserName -> User**/
	 HashMap<String, String[]> IDStatistics;    /**VoterID -> Statistics**/   
	 HashMap<String, List<Candidate>> voterIDs; /**VoterIDs -> Candidates**/ 
	 HashMap<String, List<Candidate>> votes;	/**Race -> Candidates**/
	 HashMap<String, Integer> summaryStatistcs; /**UserName -> SummaryStatistics**/
	 ArrayList<Boolean>  writeIns;
	
	public Election(String title)
	{
		election_title = title;
		votedUsers = new  HashMap<String, User>();
		votes = new HashMap<String, List<Candidate>>();
		voterIDs = new HashMap<String, List<Candidate>>();
		summaryStatistcs= new HashMap<String, Integer>();
		IDStatistics = new HashMap<String, String[]>();
		writeIns = new ArrayList<Boolean>() ;
	}
	
	public String toString()
	{
		return election_title;
	}
}
