import java.awt.Color; 
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

/**
 * @author CodeFather 
 * Login GUI for all users
 * Validates user login | Brings up UserGUI given validLogin |
 */
public class Login extends JFrame implements ActionListener{

	private static final long serialVersionUID = -4896195070277297392L;
	String inputUsername;
	String inputPassword;
	User user;
	JTextField usernameFLD;
	JTextField passwordFLD;
	JLabel tryAgain;
	JLabel electionDown;

	ObjectInputStream brIn;
	ObjectOutputStream pwOut;	
	Socket sock;
	
	 Login(){
		 /**Initialzies Server**/
		 try {
				sock = new Socket("127.0.0.1",50000);
				pwOut = new ObjectOutputStream(sock.getOutputStream());
				brIn = new ObjectInputStream(sock.getInputStream());    	
			} catch (IOException e) {
				e.printStackTrace();
			}

		JPanel mainPanel = new JPanel(new GridBagLayout());		
	    JPanel tablePanel = new JPanel(new GridBagLayout());
	    GridBagConstraints c = new GridBagConstraints();
		JPanel userPanel = new JPanel();
		JPanel passPanel = new JPanel();
		JPanel myVote = new JPanel();
		
		/**Login GUI Components**/
		 usernameFLD = new JTextField("Rnkambara",20);
		 passwordFLD = new JTextField("helloworld",20);
		 
		JLabel userLabel = new JLabel("Username: ");
		JLabel passLabel = new JLabel("Password: ");
		
		JLabel myVoteLabel = new JLabel("MyVote Login: ");
		JButton login = new JButton("Login");
		login.setIcon(MyImages.smileIcon);
		login.setIconTextGap(10);
		
		tryAgain = new JLabel();
		tryAgain.setText("Incorrect username or password. Please Try Again...");
		tryAgain.setIcon(MyImages.frownIcon);
		tryAgain.setVisible(false);
		
		electionDown = new JLabel();
		electionDown.setText("Well this is awkward... No elections present...");
		electionDown.setIcon(MyImages.xEyesIcon);
		electionDown.setVisible(false);
		
		/**Adding componets to Panels**/
		mainPanel.setBackground(MyColors.deepBlue);
		
			/*UserName Panel*/
		userPanel.add(userLabel);
		userPanel.add(usernameFLD);
		userPanel.setBackground(MyColors.beechyBlue);
		userPanel.setBorder(new LineBorder(Color.black,2));
		
			/*Password Panel*/
		passPanel.add(passLabel);
		passPanel.add(passwordFLD);
		passPanel.setBackground(MyColors.seaGreen);
		passPanel.setBorder(new LineBorder(Color.black,2));
		
			/*MyVote Panel*/
		myVote.add(myVoteLabel);
		myVote.setBackground(MyColors.paleTurquoise);
		
			/*Table Panel*/
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0; 
		c.gridy = 0;
		tablePanel.add(myVote,c);
		c.gridx = 0; 
		c.gridy = 1;
		c.insets = new Insets(8,10,8, 10);
		tablePanel.add(userPanel,c);
		c.gridx = 0;
		c.gridy = 2;
		tablePanel.add(passPanel,c);
		c.gridx = 0;
		c.gridy = 3;
		login.setActionCommand("login");
		login.addActionListener(this);
		tablePanel.add(login,c);
		c.gridx = 0;
		c.gridy = 4;
		tablePanel.add(tryAgain,c);
		c.gridy = 5;
		tablePanel.add(electionDown,c);
		tablePanel.setBorder(new LineBorder(Color.white,3));
		tablePanel.setBackground(Color.white);
		
			/*Main Panel*/
		mainPanel.add(tablePanel);

		/**Sets Defaults for main Panel**/
		this.getContentPane().add(mainPanel);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setSize(430,300);
        this.setVisible(true);  
        this.setIconImage(MyImages.codeFather.getImage());
        this.getRootPane().setDefaultButton(login);    
	}
		
	/**
	* Action Preformed Method - Checks what buttons have been pressed
	* @param e - Action event (actions enacted )
	* |Login - Grabs inputted username and password and checks if valid login
	* 		|Valid - The user's GUI is brought up
	* 		|Invalid - User is prompted to try again
	* **/
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("login"))
		{
			inputUsername = usernameFLD.getText();
			inputPassword = passwordFLD.getText();
				/**Initialize socket to access server**/
			try {

				/**Writes <login> key to server**/
				/***Reads from server to see if username has been validated (or not)**/
				pwOut.writeObject("<login> " + inputUsername + " " + inputPassword);
				String readObject = (String) brIn.readObject();
				 
				
				switch(readObject){
				/**Valid Login Case**/
				case"<validated>":
						user = (User) brIn.readObject();
						
						/**HSO does not view current elections**/
						if(user instanceof HSO)
						{
							user.UserGUI(user);
							this.setVisible(false);
						}
						/**Error check | is election up? | yes show interface | no display error message|**/
						else
							if
							(electionUp())
							{
							ElectionInterface eI = new ElectionInterface(user);	
							this.setVisible(false);
							}
						else
							electionDown.setVisible(true);
						
					break;
					/**Invalid Login Case**/
					case"<invalid>": 
						this.tryAgain.setVisible(true);
					break;
					}
				
			} catch (IOException | ClassNotFoundException e1) {
				
				e1.printStackTrace();
			}
		}
	}
	
	/**@return Check if election is up | true | false |**/
	public boolean electionUp(){
		boolean electionUp = true;
		try {
			pwOut.writeObject("<electionUp>");
			electionUp = (boolean)brIn.readObject();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return electionUp;
	}
	
	public static void main(String[]args){
		 MyVoteServer server = new MyVoteServer(true);
		 server.start();
		 server.restore();
		 
		new Login();
	}
}
