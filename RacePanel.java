import java.util.ArrayList; 
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JRadioButton;

	/**
	 * @author CodeFather 
	 * Race Panel - used to create panels that hold races and candidates
	 */
	public class RacePanel implements java.io.Serializable {

		private static final long serialVersionUID = -964492890009786601L;
		 public  JPanel race;
       	 public String race_title;
       	 public transient JRadioButton button;
       	 boolean writein;
       	 public  List<Candidate> candidates;
       	 public  List<String> names = new ArrayList<String>();
       	 
       	 /**
       	  * Constructor for Race Panel
       	  * @param panel - JPanel 
       	  * @param race_title - Title for race
       	  * @param candidates - Candidate array | holds candidates involved in race
       	  **/
       	 RacePanel(JPanel panel, String race_title, List<Candidate> candidates){
       		 this.race = panel;
       		 this.race_title = race_title;
       		 this.candidates = candidates;
       		 writein = false;
       		 for(Candidate name: candidates){
       			 names.add(name.getName());
       		 }
       	 }
       	 RacePanel(String race_title, List<Candidate> candidates){
       		 this.race_title = race_title;
       		 this.candidates = candidates;
       		 
       		 for(Candidate name: candidates){
       			 names.add(name.getName());
       		 }
       	 }
       	 
       	 /**
       	  * Takes in a JRadioButton and adds to Race Panel
       	  * @param button - Radio button to represent choice for candidate
       	  **/
       	 public void addButton(JRadioButton button){
       		 race.add(button);
       		 this.button = button;
       	 }
 
       	 /**
       	 * @return list of winning candidates
       	 * Takes int acccount ties. If there is a tie, Both
       	 * candidates are added
       	 */
       	public List<Candidate>  getWinner(){
       		Candidate temp = candidates.get(0);
       		List<Candidate> winners = new ArrayList<Candidate> ();
       		
       		for(Candidate tally: candidates){
    			if(temp.getTally() == tally.getTally()){
    				winners.add(tally);
    			}
    			
				if(temp.getTally() < tally.getTally()){
					winners.clear();
					winners.add(tally);
				}
       		}
    			
       		return winners;
       	 }
       	        	 
       	 /**ToString method for class**/
       	 public String toString(){
       		 String result = race_title + ": ";
       		 
       		 for(Candidate names: candidates)
       			 result += "|" + names.getName() + " Tally: " + names.getTally() ;
       		 return result += "|\n\n";
       	 }

      } 
