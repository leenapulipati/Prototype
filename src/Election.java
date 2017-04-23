import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Election implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	String election_title;
	Date pollStartDay;
	Date pollEndDay;

	HashMap<String, User> votedUsers;
	/** UserName -> User **/
	HashMap<String, String[]> IDStatistics;
	/** VoterID -> Statistics **/
	HashMap<String, List<Candidate>> voterIDs;
	/** VoterIDs -> Candidates **/
	HashMap<String, List<Candidate>> votes;
	/** Race -> Candidates **/
	HashMap<String, Integer> summaryStatistcs;

	/** UserName -> SummaryStatistics **/

	public Election(String title, Date start, Date end) {
		election_title = title;
		pollStartDay = start;
		pollEndDay = end;
		
		votedUsers = new HashMap<String, User>();
		votes = new HashMap<String, List<Candidate>>();
		voterIDs = new HashMap<String, List<Candidate>>();
		summaryStatistcs = new HashMap<String, Integer>();
		IDStatistics = new HashMap<String, String[]>();
		
	}

	public Date getPollStart(){
		return pollStartDay;
	}
	
	public Date getPollEnd(){
		return pollEndDay;
		
	}
	
	public String toString() {
		
		return election_title + " from " + pollStartDay + " to " + pollEndDay;
	
	}
}
