import java.io.FileInputStream;   
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MyVoteServer extends Thread  {

	static HashMap<String, Election> elections;
	static Election election;
	static HashMap<String, User> users;
	String selectedE;
  
	ServerSocket ss;
	
	/** MyVoteServer Constructor
	 * Deserializes API file to initialize the users hashmap*/
	MyVoteServer(boolean runDeserialize){
		 users = new HashMap<String,User>();
		 elections = new  HashMap<String, Election>();
		 
		 if(runDeserialize)
		 API.deserializeAPI();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 * >>>Starts the server
	 */
	public void run(){
		try{
			ss = new ServerSocket(50000);
			System.out.println("MyVote Server Started");
			
			while(true)
			{
				Socket myVote = ss.accept();
				MyVoteHandler mv = new MyVoteHandler(myVote, this);
				System.out.println("Started MyVoteHandler");
				mv.start();
			}
		}
		catch(SocketException shock)
		{	
			System.out.println("Already running");
		}
		catch(IOException e){
			System.out.println("Server Already Running");
		}
	}
	
	/**@param inputUsername
	 * @param inputPassword
	 * @return validates user | true | false | **/
	public boolean validateLogin(String inputUsername, String inputPassword){	
		return (users.containsKey(inputUsername) && inputPassword.equals(users.get(inputUsername).password));
	}
	
	/**Takes in a userName and returns boolean for if they voted
	 * @param username | Name of user to check in voter list |
	 * @return         |true user has voted | false use hasn't voted |*/
	public boolean voted(String username){
		return election.votes.votedUsers.containsKey(username);
	}
	
	/**Takes in a user and returns the user from the user list
	 * @param username | username to retrieve |
	 * @return user    | User in the list     |**/
	public User getUser(String username){
		return users.get(username.trim());
	}
	
	/**@return an array of the elections*/
	public Election[] getElections(){
		return elections.values().toArray(new Election[0]);
	}
	
	/** @return users in api**/
	public User[] getVotedUsers(){
		return  election.votes.votedUsers.values().toArray(new User[0]);
	}
	
	public HashMap<String, List<Candidate>> getVotes(){
		return election.votes.votes;
	}
	
	public Date getStart(){
		return election.pollStartDay;
	}
	
	public Date getEnd(){
		return election.pollEndDay;
	}
	
	public HashMap<String, List<Candidate>> getRecount(){
		return election.votes.votes;
	}
	
	/**@return hashmap containing the summary statisitcs */
	public HashMap<String, Integer> getStatistics()
	{
		return election.votes.summaryStatistcs;
	}
	
	public ArrayList<Boolean> getMultiVoteSelections()
	{	
		return election.votes.raceVoteSelection;
	}
	
	public Object getCertification() 
	{
		return election.certified;
	}
	
	/**@return boolean | true election is up | false no elections up | */
	public boolean electionUp(){
		return !elections.isEmpty();
	}
	
	public void certifyElection()
	{
		election.certified = true;	
		backup();
	}
	/**Takes in the name of an election assigns the attribute election to the selected election
	 * @param selected - Name of the election to select
	 * @return the election selected */
	public String selectedElection(String selected){
		selectedE = selected;
		election = elections.get(selectedE);
		return selectedE;
	}
	/**Takes in an eleciton and adds it to the server
	 * @param e - the election to be added to the server*/
	public void addElection( Election e)
	{
		elections.put(e.election_title, e);
		backup();
	}
	
	/**@param race
	 * @param candidates
	 * adds races to the HashMap which keeps track of votes
	 * votes is a hashmap with key [race = {list of candidates}]
	 * candidates has | candidate name | vote tallies |**/
	public void addRace(RacePanel race, boolean mv) 
	{
		System.out.println("selectedElection " + election);
		election.votes.votes.put(race.race_title, race.candidates);
		election.votes.raceVoteSelection.add(mv);
		System.out.println(elections);
		backup();
	}
	
	/**takes in a vote and updates the vote tally for a candidate
	 * | Maps statistcs with voter ID|
	 * | Maps candidate with voter ID|
	 * @param race      | Race in which vote is added |
	 * @param selected  | Candidate selected in race  |
	 * @param ID        | ID genderated for voted user|
	 *
	 * NOTE >>> Backs up server with each vote (testing purposes)**/
	public void addVote(String race, int selected, String votedUser, String ID)
	{
		Candidate SelectedCandidate = election.votes.votes.get(race.trim()).get(selected);
				  SelectedCandidate.incramentTally();
		election.votes.votedUsers.put(votedUser, users.get(votedUser));
		election.votes.IDStatistics.put(ID, users.get(votedUser).dataSet());
		
		if(election.votes.voterIDs.containsKey(ID))
			election.votes.voterIDs.get(ID).add(SelectedCandidate);
		else
		{
			List<Candidate> c = new ArrayList<Candidate>(); c.add(SelectedCandidate);
			election.votes.voterIDs.put(ID, c);
		}
		System.out.println(election.votes.voterIDs);
		System.out.println(election.votes.votes);
		backup();
	}
	
	
	/** Takes in Vote ID and removes ALL ID's vote from election
	 * @param ID | Voter ID |*/
	public void removeIDStatistics(String ID){
		String[] removeID = election.votes.IDStatistics.get(ID);
		
		for(String data: removeID)
			updateSummaryStatistics(data, false);
	}
	
	/** Takes in information rewired to disqualify a user and removes the user information
	 * and decrements the tally of the voted candidate
	 * 					|User - user's summary statistidcs must be removed from the summaryStastics
	 * @param userName 		| User whose statistics need to be removed             |
	 * @param raceName 		| name of the race used to access the candidate        |
	 * @param candidateName | name of the candidate who's tally must be decremented|
	 * @return              | String representing result of removal                |*/
	public String removeVote(String ID)
	{
		String result = "";
		System.out.println(election.votes.voterIDs);
		if(election.votes.voterIDs.containsKey(ID)){
			
			List<Candidate> removeCandidates = election.votes.voterIDs.get(ID);
			
			for(Candidate c: removeCandidates)
			{
				Candidate.getCandidate(election.votes.votes.get(c.race), c.name).decrementTally();
				result+= "Disqualified: " + c.name + "\n";
			}
			removeIDStatistics(ID);
			return result;
		}
		else
			return "Not Disqualified, check voter ID: " + ID;
	}

	/** Takes in the statitic (ie gender, college, etc...) 
	 * 		| increments the statistic for the election |
	 * @param data - statistic to be incremented*/
	public void updateSummaryStatistics(String data, boolean add)
	{
		if(election.votes.summaryStatistcs != null && election.votes.summaryStatistcs.containsKey(data))
		{
			int value = election.votes.summaryStatistcs.get(data.trim()).intValue();
			
			if(add == true)	
				election.votes.summaryStatistcs.put(data.trim(), value+1);
			else 
				election.votes.summaryStatistcs.put(data.trim(), value-1);
		}
		else election.votes.summaryStatistcs.put(data, 1);
		System.out.println(election.votes.summaryStatistcs);
		backup();
	}
	
	/**Serializes/saves the server's "ballot" >>> ballot panels
	 * and current votes **/
	public static void backup(){
		try
		{	
			ObjectOutputStream apiOut = new ObjectOutputStream(new FileOutputStream("BackupAPI.ser"));
			apiOut.writeObject(elections);
			apiOut.close();
			System.out.println("Backup Complete!");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**deserializes/restores the servers "ballot" >>> Votes holds 
	 * races and Candidates necessary to re-build a ballot**/
	@SuppressWarnings("unchecked")
	public static void restore(){
		
		try{
			
			ObjectInputStream apiIn = new ObjectInputStream(new FileInputStream("BackupAPI.ser"));
			elections = (HashMap<String, Election>) apiIn.readObject();
			System.out.println("Restoration Complete!");
			System.out.println(elections);
			apiIn.close();
			
		}catch(IOException e)
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e) 
		{
			System.out.println("Backup Not Found...");
			e.printStackTrace();
		}
	}
	
	/**Kills the server on exit**/
	public void die(){
		try{
			ss.close();
		}catch(IOException e){
			System.out.print("exiting...");
		}
		System.exit(0);
	}
	
	public static void resetElection(){
		new MyVoteServer(true).start();
		backup();
	}

	public static void main(String[]args){
		resetElection();
		restore();
	}


	
	
}
