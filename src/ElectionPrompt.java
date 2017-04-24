import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
public class ElectionPrompt extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	/**Server Stuff**/
	ObjectInputStream brIn;
	ObjectOutputStream pwOut;	
	Socket sock;
	
	JLabel lblElectionName;
	JLabel lblCommissionerID;
	JLabel lblPollBeginDay;
	JLabel lblPollEndDay;
	JTextField txtElectionName;
	JTextField txtCommissionerID;
	JButton confirm;
	HSOInterface election;
	PollingTimes startDay;
	PollingTimes endDay;
	
	ElectionInterface eInterface = new ElectionInterface(null);
	
	/**HSO Interface is taken through election ballot
	 * all additions are made to original HSO interface**/
	ElectionPrompt(HSOInterface HSO){
		startServer();
		/**Assign election to HSO interface**/
		election = HSO;

			JPanel panelMain = new JPanel();
			GroupLayout layout = new GroupLayout(panelMain);
			JPanel panelSub = new JPanel();
			JPanel panelSub1 = new JPanel();
			JPanel panelSub2 = new JPanel();
			startDay = new PollingTimes();
			endDay = new PollingTimes();
			
			lblElectionName = new JLabel("Election Name:  ");
			lblCommissionerID = new JLabel("Election Commissioner ID:  ");
			lblPollBeginDay = new JLabel("Date Polls Open:  ");
			lblPollEndDay = new JLabel("Date Polls Close:  ");
			
			txtElectionName = new JTextField(20);
			txtCommissionerID = new JTextField(20);
			
			confirm = new JButton("Confirm Election");
			confirm.setActionCommand("confirm");
			confirm.addActionListener(this);
		
		/**Set Defaults for main panel | allows enter key | top bar icon**/
        this.getRootPane().setDefaultButton(confirm);
        this.setIconImage(MyImages.codeFather.getImage());
        this.setTitle("Create Election");
		this.setSize(800, 200);
        this.getContentPane().add(panelMain);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        panelMain.setBackground(MyColors.deepBlue);
        
        panelSub.add(lblElectionName);
        panelSub.add(txtElectionName);
        
        panelSub1.add(lblCommissionerID);
        panelSub1.add(txtCommissionerID);
        
        panelSub2.add(lblPollBeginDay);
		panelSub2.add(startDay);
		panelSub2.add(lblPollEndDay);
		panelSub2.add(endDay);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
        		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
        				.addComponent(panelSub)
        				.addComponent(panelSub1)
        				.addComponent(panelSub2)
        				.addComponent(confirm))
        		);
        
        layout.setVerticalGroup(layout.createParallelGroup()
        		.addGroup(layout.createSequentialGroup()
        				.addComponent(panelSub)
        				.addComponent(panelSub1)
        				.addComponent(panelSub2)
        				.addComponent(confirm))
        		);
        
        /**Centers GUI onto Screen**/
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((d.getWidth() - this.getWidth()) / 2);
        int y = (int) ((d.getHeight() - this.getHeight()) / 2);
        setLocation(x, y);
        this.setVisible(true); 
	}
	
	public void actionPerformed(ActionEvent e)
	{
		/**User confirms Created Electin | Election title and EC ID are retrieved      |
		 * 								 | Election is added to HSO and User Interface | 
		 * 								 | Election is uploaded to server              |
		 * **/
		if(e.getActionCommand().equals("confirm"))
		{
			startDay.actionPerformed(e);
			endDay.actionPerformed(e);
			
			//default start time is 7am on the start day and end time is 12am on the end day
			startDay.day.setHours(7);
			startDay.day.setMinutes(0);
			startDay.day.setSeconds(0);
			endDay.day.setHours(0);
			endDay.day.setMinutes(0);
			endDay.day.setSeconds(0);
			
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
			
			String electionName = txtElectionName.getText();
			
			int dialogResult = JOptionPane.showConfirmDialog(null, "The election's name is listed as "+electionName.toUpperCase()+", it is scheduled"
					+ " to begin on " + sdf.format(startDay.day) + " and end on " + sdf.format(endDay.day) + "\n do you wish to continue?");
			
			
			String commissionerID = txtCommissionerID.getText();
			
			
			if(dialogResult == JOptionPane.YES_OPTION)
			{
				election.addCommissioner(commissionerID, electionName);
				election.addList(electionName);
				eInterface.addElection(electionName);
				eInterface.setVisible(false);
				this.setVisible(false);
				uploadElection(eInterface);
			}
			else
				System.out.println("Hmmm, well too bad!");
		}
	}
	
	/**
	 * Adds election to server by providing Election
	 * @param e Election Interface*/
	public void uploadElection(ElectionInterface e)
	{
		try {
			pwOut.writeObject("<addElection>");
			pwOut.writeObject(new Election(txtElectionName.getText(), startDay.day, endDay.day));	
		} catch (IOException e1) 
		{
			e1.printStackTrace();
		}
		
	}
	
	public void die()
	{
		try {
			pwOut.writeObject("<shutdown>");
			this.setVisible(false);
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	 public void startServer()
	 {
		 /**Initialzies Server**/
		 try {
				sock = new Socket("127.0.0.1",50000);
				pwOut = new ObjectOutputStream(sock.getOutputStream());
				brIn = new ObjectInputStream(sock.getInputStream());    	
			
		 	} catch (IOException e) {
				e.printStackTrace();
			}
	 }
	
	

}
