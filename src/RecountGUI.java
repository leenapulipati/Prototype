import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

public class RecountGUI extends JFrame implements ActionListener{

	ObjectInputStream brIn;
	ObjectOutputStream pwOut;	
	Socket sock;
	
	RecountGUI() {
		
		 startServer();
		
		 HashMap<String, List<Candidate>> recountElections =getRecountDetails();
		 String[] race = recountElections.keySet().toArray(new String[0]);
		 List<Candidate>[] candidates = recountElections.values().toArray(new List[0]);
		 	 
		 JPanel mainPanel = new JPanel(new GridBagLayout());	
		 mainPanel.setBackground(Color.white);
		 JPanel recountPanel = new JPanel(new GridBagLayout());
		 JPanel raceP = new JPanel();
		 JPanel candidatesP = new JPanel();
		 raceP.setBackground(MyColors.paleTurquoise);
		 GridBagConstraints c = new GridBagConstraints();
		 GridBagConstraints v = new GridBagConstraints();
		 
		 
		 c.fill = GridBagConstraints.HORIZONTAL;
		 c.insets = new Insets(9,10,9, 10);
		 v.fill = GridBagConstraints.VERTICAL;
		 v.insets = new Insets(9,10,9, 10);
		 
		 /**Adds winners of race go GUI display**/
		 
		 JButton trophy = new JButton();
		 trophy.setText("Recount of the Election Races!  ");
		 
		 trophy.setBackground(Color.white);
		 trophy.setBorder(new LineBorder(MyColors.gold,3));
		 trophy.setIconTextGap(4);
		 c.gridy = 1;
		 mainPanel.add(trophy,v);
		 
		 int k=0;
		 for(int i = 0; i< race.length; i++)
		 {
			 raceP.setBorder(new LineBorder(MyColors.gold,4));
			 raceP.add(new JLabel(race[i]));		 
			 
			 for(int j=0;j< candidates[i].size();j++)
			 {
				 c.gridy = i+4;	
				 
			 candidatesP.setBorder(new LineBorder(MyColors.gold,4));
			 candidatesP.add(new JLabel(candidates[i].get(j).getName()));
			 candidatesP.add(new JLabel(String.valueOf(candidates[i].get(j).getTally())));
			 raceP.add(candidatesP,c);
			 candidatesP = new JPanel();
			 
			 }
			 
			 recountPanel.add(raceP,c);
			 raceP = new JPanel();
			 raceP.setBackground(MyColors.paleTurquoise);
		 }
		 
		 
    	 JButton finish = new JButton("Finish");
         finish.setActionCommand("finish");
         finish.addActionListener(this);
         finish.setBorder(new LineBorder(MyColors.kaki,4));
         finish.setBackground(Color.white);
		
		 c.gridy = k+2;
		
		
		 c.gridy = 1;
		 mainPanel.add(recountPanel,c);
		 mainPanel.add(finish,v);
		 mainPanel.setBackground(MyColors.beechyBlue);
		 mainPanel.setBorder(new LineBorder(MyColors.gold,4));
		 
		 /**Sets Defaults for main Panel**/ 
		 this.getContentPane().add(mainPanel);
		 this.getRootPane().setDefaultButton(finish);
		 this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  
         this.setIconImage(MyImages.codeFather.getImage());
         this.setTitle("Recount Results");
         this.setSize(700, 400);
         
         /**Centers GUI onto Screen**/
         Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
         int x = (int) ((d.getWidth() - this.getWidth()) / 2);
         int y = (int) ((d.getHeight() - this.getHeight()) / 2);
         setLocation(x, y);
         this.setVisible(true);  
	        
	        
	}
	 public void actionPerformed(ActionEvent e) {
			/**Confirms completed submission
	    	 * exits server and system**/
	    	if(e.getActionCommand().equals("finish"))
	    		this.setVisible(false);
		} 
	 /**
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * Retrieves array race Panels from Server
	 */
	public HashMap<String, List<Candidate>> getRecountDetails() {
		HashMap<String, List<Candidate>> candidatesRecount = null;
		try {
			pwOut.writeObject("<getRecount>");
			candidatesRecount = (HashMap<String, List<Candidate>>)brIn.readObject();		
			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return candidatesRecount;
	 }
	 
	 /**Starts Server Components**/
	public void startServer(){
		 /**Initialzies Server**/
		 try {
				sock = new Socket("127.0.0.1",50000);
				pwOut = new ObjectOutputStream(sock.getOutputStream());
				brIn = new ObjectInputStream(sock.getInputStream());    	
			} catch (IOException e) {
				
				e.printStackTrace();
			}
	 }
	 
	 public static void main(String[]args){
		 MyVoteServer server = new MyVoteServer(true);
		 server.start();
		 server.restore();
		 
		 new RecountGUI();
	 }

	
}
