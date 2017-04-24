import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
public class ElectionPrompt extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	/**Server Stuff**/
	ObjectInputStream brIn;
	ObjectOutputStream pwOut;	
	Socket sock;
	
	JLabel lblElectionName;
	JLabel lblCommissionerID;
	JTextField txtElectionName;
	JTextField txtCommissionerID;
	JButton confirm;
	HSOInterface election;
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
		
		lblElectionName = new JLabel("Election Name:  ");
		lblCommissionerID = new JLabel("Election Commissioner ID:  ");
		
		txtElectionName = new JTextField(20);
		txtCommissionerID = new JTextField(20);
		
		confirm = new JButton("Confirm Election");
		confirm.setActionCommand("confirm");
		confirm.addActionListener(this);
		
		/**Set Defaults for main panel | allows enter key | top bar icon**/
        this.getRootPane().setDefaultButton(confirm);
        this.setIconImage(MyImages.codeFather.getImage());
        this.setTitle("Create Election");
        this.setSize(450, 200);
        this.getContentPane().add(panelMain);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        panelMain.setBackground(MyColors.deepBlue);
        
        panelSub.add(lblElectionName);
        panelSub.add(txtElectionName);
        
        panelSub1.add(lblCommissionerID);
        panelSub1.add(txtCommissionerID);
        
        layout.setHorizontalGroup(layout.createSequentialGroup()
        		.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
        				.addComponent(panelSub)
        				.addComponent(panelSub1)
        				.addComponent(confirm))
        		);
        
        layout.setVerticalGroup(layout.createParallelGroup()
        		.addGroup(layout.createSequentialGroup()
        				.addComponent(panelSub)
        				.addComponent(panelSub1)
        				.addComponent(confirm))
        		);
        
        /**Centers GUI onto Screen**/
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((d.getWidth() - this.getWidth()) / 2);
        int y = (int) ((d.getHeight() - this.getHeight()) / 2);
        setLocation(x, y);
        this.setVisible(true); 
	}
	
	public void actionPerformed(ActionEvent e){
		/**User confirms Created Electin | Election title and EC ID are retrieved      |
		 * 								 | Election is added to HSO and User Interface | 
		 * 								 | Election is uploaded to server              |
		 * **/
		if(e.getActionCommand().equals("confirm")){
			
			String electionName = txtElectionName.getText();
			election.addList(electionName);
			eInterface.addElection(electionName);
			this.setVisible(false);
			uploadElection(eInterface);
		}
	}
	
	/**
	 * Adds election to server by providing Election
	 * @param e Election Interface*/
	public void uploadElection(ElectionInterface e){
		try {
			pwOut.writeObject("<addElection>");
			pwOut.writeObject(new Election(txtElectionName.getText()));	
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
	}
	public void die(){
		try {
			pwOut.writeObject("<shutdown>");
			this.setVisible(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	
	

}
