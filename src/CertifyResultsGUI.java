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
import java.util.HashMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

public class CertifyResultsGUI extends JFrame implements ActionListener {

	User[] users;
	HashMap<String, Integer> summary;
	ObjectInputStream brIn;
	ObjectOutputStream pwOut;	
	Socket sock;
	
	CertifyResultsGUI(){
		
		startServer();
		/**Retrieves and initializes array of users from server**/
		certify();
		/**Panel Declarations**/
		 JPanel mainPanel = new JPanel(new GridBagLayout());
		 JPanel resultsPanel = new JPanel(new GridBagLayout());
         JPanel userP = new JPanel();
         userP.setBackground(Color.black);
         GridBagConstraints c = new GridBagConstraints();
        
	   	JButton certify = new JButton("Certify");
	   	certify.setActionCommand("certify");
	   	certify.addActionListener(this);
	   
	    c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(9,10,9, 10);
		c.gridy = -1;

		int i;
		/**Loops through all users and retrieves demographic information**/
	   	for(i = 0; i < users.length; i++)
	   	{
	   		c.gridy = i;
	   		
	   		 userP.add(new JLabel(users[i].toString()));
	   		 userP.setBackground(MyColors.kaki);
	   		 userP.setBorder(new LineBorder(Color.black,4));
	   		 resultsPanel.add(userP,c);
	   		 userP = new JPanel();
	   	}
	   	
	   	c.gridy = i+1;
	   	getSummary();
	   	
		resultsPanel.add(new JLabel(createSummary(summary)),c);
		
	   	c.gridy = i+2;
	   	resultsPanel.add(certify,c);
	   	resultsPanel.setBackground(Color.white);
	   	resultsPanel.setBorder(new LineBorder(Color.black,4));
	   	mainPanel.add(resultsPanel);
	   	
	    /**Set Defaults for main panel | allows enter key | top bar icon**/
        this.getRootPane().setDefaultButton(certify);
        this.setIconImage(MyImages.codeFather.getImage());
        this.setTitle("Summary Statistics");
        this.setSize(550, 700);
        this.getContentPane().add(mainPanel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainPanel.setBackground(Color.white);
        
        /**Centers GUI onto Screen**/
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((d.getWidth() - this.getWidth()) / 2);
        int y = (int) ((d.getHeight() - this.getHeight()) / 2);
        setLocation(x, y);
        this.setVisible(true);  
     
	}
	
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
	 public void actionPerformed(ActionEvent e) 
		{	
		if(e.getActionCommand().equals("certify"))	
			{	
			this.setVisible(false);	
			certifyElection();
			}
		}
	 
	/**<<SERVER CONNECTION>> <certify>
	 * Retrieves and initializes array of users from server**/
	
	 public void certifyElection()
	 {
		 try 
			{
				pwOut.writeObject("<certifyElection>");
			} 
			catch (IOException e) {	e.printStackTrace();	}
	 }
	 public void certify()
	{
		try 
		{
			pwOut.writeObject("<certify>");
			users = (User[]) brIn.readObject();
		} 
		catch (IOException | ClassNotFoundException e) {	e.printStackTrace();	}
	}
	/**<<SERVER CONNECTION>> <getStatistics>
	 * Initialises a summary of the statistis | hashmap: Statistic -> data | */
	@SuppressWarnings("unchecked")
	public void getSummary()
	{
		try 
		{
			pwOut.writeObject("<getStatistics>");
			summary = (HashMap<String, Integer>)brIn.readObject();
		} 
		catch (IOException | ClassNotFoundException e) {	e.printStackTrace();	}
	}
	
	/**
	 * Creates a string representation for the summary statistics
	 * @param s | hashmap of summary statistics |
	 * @return
	 */
	public String createSummary(HashMap<String, Integer> s)
	{
		String[] keys = s.keySet().toArray(new String[0]);
		String result = "";
		
		for(String k: keys)
		{
			if(s.get(k).intValue() != 0)	
				{result += k + ": " + s.get(k).intValue() + "   ";}
		}
		return result;
	}
	
	public static void main(String[]args){

	}
}
