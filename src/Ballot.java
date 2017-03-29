import java.awt.Color;     
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

/**
 * @author CodeFather 
 * Creates ballet given input from Ballot Prompt 
 */ 
public class Ballot extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	
		JButton confirm; 
		JButton finish;
        /**Layout and ButtonGroup Components**/
        JPanel panelMain = new JPanel();
        GroupLayout layout = new GroupLayout(panelMain);
        
        /**Collections**/
        ArrayList<ButtonGroup> selectedCandidates = new ArrayList<ButtonGroup>();
        ArrayList<String> raceGroup = new ArrayList<String>();
        ArrayList<RacePanel> panels = new ArrayList<RacePanel>();
        List<String> candidates;
        HashMap<String,List<String>> raceCands = new HashMap<String,List<String>>() ;
        
        /**Server Stuff**/
    	ObjectInputStream brIn;
    	ObjectOutputStream pwOut;	
    	Socket sock;
       
	    /**
	     * Constructor - Ballot GUI
	     */
	    Ballot(){
	    	
	   	 /**Initialzies Server**/  	
	    	try {
				sock = new Socket("127.0.0.1",50000);
				pwOut = new ObjectOutputStream(sock.getOutputStream());
				brIn = new ObjectInputStream(sock.getInputStream());    	
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		
	    	 confirm = new JButton("Confirm");
	    	 confirm.setActionCommand("confirm");
	    	 confirm.addActionListener(this);
	    	 
	    	 finish = new JButton("Finish");
	         finish.setActionCommand("finish");
	         finish.addActionListener(this);
	    	
	    	/**Default values for Main Panel | Color | Size | Icon | Title | **/
	        panelMain.setBackground(MyColors.deepBlue);
	        this.setSize(400,200);
	        this.setIconImage(MyImages.codeFather.getImage());
	        this.setTitle("MyVote");
	        this.getContentPane().add(panelMain);
	        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	        this.getRootPane().setDefaultButton(confirm);
	       
	        /**Centers GUI to center of screen**/
	        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
	        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
	        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
	        setLocation(x, y);
	        setVisible(true);
	    }
	    
	    /**
	     * Adds a race panel to Ballot GUI
	     * @param p - Race Panel
	     * retrieves all ballot information from RacePanel
	     * 		| race_title | candidates |
	     */
	    public void addBallot(RacePanel p){
	    	
	    	/**panels - acts as storage for a "ballot"
	    	 | holds all necessary components to re-create ballot |
	    	 * Adds every ballot race panel to panel array**/
	    	 panels.add(p);

	    	 
	    	 ButtonGroup btnRadioGroup = new ButtonGroup();
	    	 JPanel race = p.race;
	    	 
	    	 /**Making pretty panels....**/
	    	 race.setBackground(MyColors.seaGreen);
	    	 race.setBorder(new LineBorder(Color.white,3));
	    	 race.add(new JLabel(p.race_title));
	    	 
	    	 /**holds all race titles**/
	    	 raceGroup.add(p.race_title);
	    	 
	    	 /**retrieves current list of candidates (for current race)**/
	    	 candidates = p.candidate_names;
	    	 
	    	 /**Adds to hashmap of [race = {candidates}]
	    	  * ultimate goal - assist in retrieving index of candidate 
	    	  * to tally**/
	    	 raceCands.put(p.race_title, candidates);
	    	 
	      	 int num_candidates  = candidates.size();

	      	 /**<<<SERVER CONNECTOIN ADDRACE>>>
	      	  * sends <addRace> command to server
	      	  * ultimate goald | update the candidates voting array |
	      	  * **/
	      	try {
	 	    	pwOut.writeObject("<addRace>");
	 	    	pwOut.writeObject(p.race_title);
	 	    	pwOut.writeObject(candidates);
			} catch (IOException j) {
				
				j.printStackTrace();
			}
	      	 
	      	for(int i = 0; i < num_candidates;i++){
	    		   JRadioButton cand = new JRadioButton(candidates.get(i));
	    		   cand.setActionCommand(candidates.get(i));
	    		   cand.setBackground(Color.white);
	    		   
	    		   /**Adds candidates to button group
	    		    * (only allows for selection of one candidate)**/
	    		   btnRadioGroup.add(cand);
	    		   p.addButton(cand);
	    		   p.button.setText(candidates.get(i));
	    		   p.button.setVisible(true);
	    		   
	    		   /**Addds components to main layout**/
	    		   layout.setHorizontalGroup(layout.createSequentialGroup()
		        			  .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		        					  .addComponent(p.race)
		        					  ));
		   	 	    layout.setVerticalGroup(
		   	 	            layout.createParallelGroup()
		   	 	                .addGroup(layout.createSequentialGroup())
		   	 	                    .addComponent(p.race)
		   	 	                    );      
	    	   }
	    	   selectedCandidates.add(btnRadioGroup);
	    }
	    
	    /**
	     * Adds Finish Button to Ballot GUI
	     * saves ballot to server
	     */
	    public void finishBallot(){
	    	/**Addds components to main layout**/
 		   layout.setHorizontalGroup(layout.createSequentialGroup()
	        			  .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
	        					  .addComponent(confirm)
	        					  .addComponent(finish)
	        					  ));
   	 	    layout.setVerticalGroup(
   	 	            layout.createParallelGroup()
   	 	                .addGroup(layout.createSequentialGroup())
   	 	                    .addComponent(confirm)
   	 	                    .addComponent(finish)
   	 	                    ); 
   	 	    
   	 	    /**
   	 	  	  * <<<SERVER CONNECTOIN  <saveballot> >>>
	      	  * saves completed ballot components to server
	      	  * **/    
   	 	    try {
	 	    	pwOut.writeObject("<saveballot>");
				pwOut.writeObject(panels);
			
			} catch (IOException e) {
				e.printStackTrace();
			}
  
	    }
	    
	    public void actionPerformed(ActionEvent e){
	    	
	    	/**Confirms completed submission
	    	 * exits server and system**/
	    	if(e.getActionCommand().equals("finish"))
				try {
					pwOut.writeObject("<shutdown>");
					System.exit(0);
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
	    	
	    	
	    	/**Sends vote to server**/
	    	if(e.getActionCommand().equals("confirm"))
	    	for(int i = 0; i < raceGroup.size();i++){
	    		
	    		String race = raceGroup.get(i);
	    		String selected = selectedCandidates.get(i).getSelection().getActionCommand();
	    		

	   	 	    /**
	   	 	  	  * <<<SERVER CONNECTOIN  <vote> >>>
		      	  * saves user's candidate vote to server
		      	  * **/  	
	    		try {
		 	    	pwOut.writeObject("<vote>");
		 	    	pwOut.writeObject(race);
					pwOut.writeObject(raceCands.get(race).indexOf(selected));
				} catch (IOException j) {
					
					j.printStackTrace();
				}

	    		System.out.println(race + " " + selected);
	    		
	    	}
	    }
	 
	    
	    public static void main(String args[]){
	    	
	    }	
	
}
