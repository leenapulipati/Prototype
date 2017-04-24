import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;

public class ElectionInterface extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	DefaultListModel<String> model = new DefaultListModel<String>();
	JList<String> list = new JList<String>(model);
	Election[] elections;
	Date today = new Date();
	
	/**Layout and ButtonGroup Components**/
    JPanel panelMain = new JPanel();
    JPanel pnlList = new JPanel();
    GroupLayout layout = new GroupLayout(panelMain);
    JButton confirm;
    JButton exit; 
	JLabel votedError, certificationError;
	JTextArea pollingError;
    User user;
    
    /**Server Stuff**/
	ObjectInputStream brIn;
	ObjectOutputStream pwOut;	
	Socket sock;
    
	ElectionInterface(User user){
		startServer();
		this.user = user;
		confirm = new JButton("Confirm");
    	confirm.setActionCommand("confirm");
    	confirm.addActionListener(this);
    	
    	exit = new JButton("Exit");
    	exit.setActionCommand("exit");
    	exit.addActionListener(this);
    	 
    	votedError = new JLabel();
		votedError.setText("Oops! Seems as if you've already voted!");
		votedError.setIcon(MyImages.yellowFrown);
		votedError.setForeground(Color.white);
		votedError.setVisible(false);
		
		certificationError = new JLabel();
		certificationError.setText("Certification Error... Apologies.");
		certificationError.setIcon(MyImages.yellowFrown);
		certificationError.setForeground(Color.white);
		certificationError.setVisible(false);
		
		pollingError = new JTextArea();
		pollingError.setEditable(false);
		pollingError.setForeground(Color.white);
		pollingError.setFont(new Font("Ariel", Font.BOLD,12));
		pollingError.setBackground(MyColors.deepBlue);
		pollingError.setVisible(false);
    	
    	list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
 		list.setVisibleRowCount(4);
 		list.setVisible(true);
 		 
 		pnlList.add(list);
 		pnlList.add(confirm);
		pnlList.setAutoscrolls(true);
		pnlList.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		pnlList.setLayout(new BoxLayout(pnlList, BoxLayout.Y_AXIS));
		
		getElections();
		for(Election e: elections)
		{
			this.addElection(e.election_title);
		}
		addConfirm();
		
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

	public void actionPerformed(ActionEvent e) {
		/**If user confirms Election selection | selected election is written to server    |
		 * 									   | User is checked if voted                  |
		 * 									   | User GUI brought up for selected Election |**/
		
		if(e.getActionCommand().equals("confirm") && !list.isSelectionEmpty()){
			String selectedE = list.getSelectedValue().toString();
			Election selected = null;
			
			for(int i = 0; i < elections.length; i++)
			{
				if(selectedE.equals(elections[i].election_title)) selected = elections[i];
				System.out.println(elections[i].pollStartDay);
			}
			
			try {
				pwOut.writeObject("<selectedElection>");
				pwOut.writeObject(selectedE);
				
				if(user instanceof ElectionCommissioner)
				user.UserGUI(user);
				
				if(pollsEnded())
				{
					if(certified())
					{
					new DisplayGUI();
					this.setVisible(false);
					}
					else
					{certificationError.setVisible(true);}	
				}
				
				else if
				(!hasVoted(user.username) && pollsOpen())
				{
				user.UserGUI(user);
				this.setVisible(false);
				}
				
				else
				{/**Displays error message if user has voted**/
					votedError.setVisible(hasVoted(user.username));
					
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					
					pollingError.setText("Oops! The polls are not open!\nPolls are active for this election between\n[ " + 
							sdf.format(getStart()) + " at " + "7:00am" + " to " + sdf.format(getEnd()) + " ]");
					
					pollingError.setVisible(!pollsOpen());
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		/**User selects exit button | program exits |**/
		if(e.getActionCommand().equals("exit"))
			shutdown();
	}

	public boolean pollsOpen()
	{return (getEnd().after(today) && getStart().before(today));}
	
	public boolean pollsEnded()
	{
		return(getEnd().before(today));
	}
	public Date getEnd()
	{
		Date pickle = null;
		try 
		{
			pwOut.writeObject("<End>");
			pickle = (Date) brIn.readObject();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return pickle;
	}
	public Date getStart()
	{
	Date duck = null;
		
		try 
		{
			pwOut.writeObject("<Start>");
			duck = (Date) brIn.readObject();
		} catch (Exception e) 
		{
			System.err.println(e.getMessage());
		}
		return duck;
	}
	
	public boolean certified()
	{
		boolean certified = false;
		try 
		{
			pwOut.writeObject("<checkCertification>");
			certified = (boolean)brIn.readObject();
		} catch (Exception e) 
		{
			System.err.println(e.getMessage());
		}
		return certified;
	}
	
	/**
	 * Creates GUI that lists all available elections
	 * @param eName - name of election
	 */
	public void addElection(String eName) 
	{
		model.addElement(eName);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(pnlList))
				);

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(pnlList))
				);
	}
	
	/**
	 * @param username - name of user
	 * @return check of user has voted or not | true | false |
	 */
	public boolean hasVoted(String username){
	boolean voted = true;
		try {
			pwOut.writeObject("<hasVoted>");
			pwOut.writeObject(username);
			//System.out.println((String)brIn.readObject());
			voted = (boolean)brIn.readObject();
		} catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		return voted;
	}
	
	/**
	 * Adds Confimation and Exit buttons to end of GUI
	 */
	public void addConfirm(){
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(confirm))
						.addComponent(exit)
						.addComponent(votedError)
						.addComponent(certificationError)
						.addComponent(pollingError)
				
				);

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(confirm))
						.addComponent(exit)
						.addComponent(votedError)
						.addComponent(certificationError)
						.addComponent(pollingError)
				);
	}
	
	/**Retreves array of current Elections from Server**/
	public void getElections(){
		try {
			pwOut.writeObject("<getElections>");
			elections = (Election[]) brIn.readObject();
		} catch (IOException | ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**Starts Server**/
	public void startServer(){
		 /**Initialzies Server**/
		 try {
				sock = new Socket("127.0.0.1",50000);
				pwOut = new ObjectOutputStream(sock.getOutputStream());
				brIn = new ObjectInputStream(sock.getInputStream());    	
			
		 	} catch (IOException e) 
		 	{
				e.printStackTrace();
			}
	 }
   
   /**Disconnects Socket and server 
    * Shuts down system**/
	
	public void shutdown()
	{
		   try 
		   {
			pwOut.writeObject("<shutdown>");
			System.exit(0);
		   } 
		   catch (IOException e1) 
		   {				
				e1.printStackTrace();
		   }
	   }
	}

	
