import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Candidate implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	public String name;
	public String race;
    private int tally;
	boolean writeIn; 
	
	Candidate(String name, String race) {
		this.name = name;
		this.race = race;
		writeIn = false; 
		tally = 0;
		
	}
	
	public void incramentTally(){
		tally++;
	}
	public void decrementTally(){
		tally = tally-1;
	}
	
	public int getTally(){
		return tally;
	}
	
	public String getName(){
		return name;
	}
	
	/**Takes in a list of candidats and returns the "winners"- candidats with
	 * the most tallies**/
	public static List<Candidate> GetWinners(List<Candidate> candidates){
		Candidate max = candidates.get(0);
   		List<Candidate> winners = new ArrayList<Candidate> ();
   		
   		for(Candidate tally: candidates){
			if(max.getTally() == tally.getTally()){
				winners.add(tally);
			}
			if(max.getTally() < tally.getTally()){
				max = tally;
				winners.clear();
				winners.add(tally);
			}
   		}
			
   		return winners;
	}
	
	/**
	 * Helper method for retrieving candidates from a list of candidates
	 * @param candidates | list of candidates to be searched |
	 * @param search	 | search key (name of candidate)    |
	 * @return*/
	public static Candidate getCandidate(List<Candidate> candidates, String search){
		Candidate result = null;
		
		for(Candidate name : candidates)
		{
			System.out.println(name.name.trim() + " " + search.trim());
			if(name.name.trim().equals(search.trim()))
				result = name;
		}
		return result;
	}
	
	/**
	 * Helper method which takes in a list of candidates and determines 
	 * if a candidate is present
	 * @param candidates | list of candidates to be searched |
	 * @param search	 | search key (name of candidate     |
	 * @return*/
	public static boolean contains(List<Candidate> candidates, String search){
		boolean result = false;
		for(Candidate name : candidates)
		{
			if(name.name.trim().equals(search.trim())){
				result =  true;
			}
		}
		return result;
	}
	
	/**Creates and returns a hashmap containing all of the candidates tallies
	 * @param candidates | list of candidates to be recounted |
	 * @return hashmap containing a candidate -> tally recount
	 */
	public static HashMap<String,Integer> RecountResults(List<Candidate> candidates){
		HashMap<String,Integer> recountResults = new HashMap<String,Integer>();
		
		for(Candidate candidate: candidates)
		{
			recountResults.put(candidate.getName(), candidate.getTally());
		}
		return recountResults;
	}
	
	/**Creastes an array list of candidates given string of names
	 * @param candidates | array of candidate names |
	 * @param race		 | name of the race         |
	 * @return
	 */
	public static List<Candidate> createCandidates( String[] candidates, String race) {
		ArrayList<Candidate> c = new ArrayList<Candidate>();
		
		for(String name: candidates)
			c.add(new Candidate(name,race));
		
		return c;
	}

	public String toString(){
		String result = name + " " + tally;
		return result;
	}
}
