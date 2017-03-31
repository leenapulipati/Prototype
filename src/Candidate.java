import java.util.ArrayList; 
import java.util.List;

public class Candidate implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	public String name;
	private int tally;
	
	Candidate(String name) {
		this.name = name;
		tally = 0;
	}
	
	/**
	 * Increments current tally for candidate
	 */
	public void incramentTally(){
		tally++;
	}
	public int getTally(){
		return tally;
	}
	
	public String getName(){
		return name;
	}
		
	public static List<Candidate> createCandidates( String[] a) {
		ArrayList<Candidate> c = new ArrayList<Candidate>();
		for(String name: a)
			c.add(new Candidate(name));

		return c;
	}

	public String toString(){
		String result = name ;
		return result;
	}
}