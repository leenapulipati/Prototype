import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

/**
 * @author CodeFather Creates ballet given input from Ballot Prompt
 */
public class Ballot extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	boolean multivote;
	ArrayList<Boolean> mPref = new ArrayList<>();
	
	JButton confirm;
	JButton finish;
	/** Layout and ButtonGroup Components **/
	JPanel panelMain = new JPanel();
	GroupLayout layout = new GroupLayout(panelMain);
	Election e;
	User user;

	/** Collections **/
	ArrayList <ButtonGroup> selectedCandidate = new ArrayList<ButtonGroup>();
	ArrayList <JCheckBox> selectedCandidates = new ArrayList<>();
	ArrayList<RacePanel> panels = new ArrayList<RacePanel>();

	/** Server Stuff **/
	ObjectInputStream brIn;
	ObjectOutputStream pwOut;
	Socket sock;

	/*************
	 * Constructor - Ballot GUI
	 * 
	 * @param user
	 *            - user currently using ballot
	 ***********/
	Ballot(User user) {

		/** Initialzies Server **/
		startServer();
		this.user = user;

		confirm = new JButton("Confirm");
		confirm.setActionCommand("confirm");
		confirm.addActionListener(this);

		finish = new JButton("Finish");
		finish.setActionCommand("finish");
		finish.addActionListener(this);

		/** Default values for Main Panel | Color | Size | Icon | Title | **/
		panelMain.setBackground(MyColors.deepBlue);
		this.setSize(400, 200);
		this.setIconImage(MyImages.codeFather.getImage());
		this.setTitle("MyVote");
		this.getContentPane().add(panelMain);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.getRootPane().setDefaultButton(confirm);

		/** Centers GUI to center of screen **/
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
		setLocation(x, y);
		setVisible(true);
	}

	/**
	 * Adds a race panel to Ballot GUI
	 * 
	 * @param p
	 *            - Race Panel retrieves all ballot information from RacePanel |
	 *            race_title | candidates |
	 */
	public void addBallot(RacePanel p, boolean save, boolean multivote) {

		/**
		 * Panels - used to compartmentalize necessary ballot components race
		 * and candidates
		 **/
		
		mPref.add(multivote);
		
		this.multivote = multivote;
		panels.add(p);
		/** Saves race and candidates to server **/
		addRace(p, multivote);

		JPanel race = new JPanel();
		race.setBackground(MyColors.seaGreen);
		race.setBorder(new LineBorder(Color.white, 3));
		race.add(new JLabel(p.race_title));

		if(multivote == false){
		ButtonGroup btnRadioGroup = new ButtonGroup();
		
		int num_candidates = p.candidates.size();

		/** Loop goes through and creates ballot panels with all candidates **/
		for (int i = 0; i < num_candidates; i++) {

			JRadioButton cand = new JRadioButton(p.candidates.get(i).getName());
			cand.setText(p.candidates.get(i).getName());
			cand.setVisible(true);
			cand.setActionCommand(p.candidates.get(i).getName());
			cand.setBackground(Color.white);
			btnRadioGroup.add(cand);
			race.add(cand);

			/** Adds components to main layout **/
			layout.setHorizontalGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(race)));
			layout.setVerticalGroup(
					layout.createParallelGroup().addGroup(layout.createSequentialGroup()).addComponent(race));
		}
		selectedCandidate.add(btnRadioGroup);
		
		}else{
			
			ArrayList <JCheckBox> checkGroup = new ArrayList<>();
			
			int num_candidates = p.candidates.size();

			/** Loop goes through and creates ballot panels with all candidates **/
			for (int i = 0; i < num_candidates; i++) {

				JCheckBox cand = new JCheckBox(p.candidates.get(i).getName());
				cand.setText(p.candidates.get(i).getName());
				cand.setVisible(true);
				cand.setActionCommand(p.candidates.get(i).getName());
				cand.setBackground(Color.white);
				selectedCandidates.add(cand);
				race.setLayout(new FlowLayout());
				race.add(cand);

				/** Adds components to main layout **/
				layout.setHorizontalGroup(layout.createSequentialGroup()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(race)));
				layout.setVerticalGroup(
						layout.createParallelGroup().addGroup(layout.createSequentialGroup()).addComponent(race));
			}
			
		}
	}

	/** Adds Finish and confirm Button to Ballot GUI **/
	public void finishBallot(boolean save) {
		
		
		/** Adds components to main layout **/
		layout.setHorizontalGroup(layout.createSequentialGroup().addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(confirm).addComponent(finish)));
		layout.setVerticalGroup(layout.createParallelGroup().addGroup(layout.createSequentialGroup())
				.addComponent(confirm).addComponent(finish));
	}

	public void actionPerformed(ActionEvent e) {
		/**
		 * User selects finish button | system disconnects from server and
		 * closes |
		 **/
		if (e.getActionCommand().equals("finish"))
			shutdown();

		/**
		 * User confirms selections | race title and selected candidate
		 * regrieved | | Votes are added to system | | User's summary statistics
		 * are added |
		 **/
		if (e.getActionCommand().equals("confirm"))

		{
			String ID = (UUID.randomUUID().toString().substring(0, 7));
			JFrame frame = new JFrame();
			JOptionPane.showMessageDialog(frame, "Voter ID: " + ID, "Very important voter certificate ID... Thing...",
					JOptionPane.PLAIN_MESSAGE, MyImages.codeFather);
			
			String selectedC = null;
			for (int i = 0; i < panels.size(); i++) {

				String race = panels.get(i).race_title;
				if(multivote == false){
					selectedC = selectedCandidate.get(i).getSelection().getActionCommand();
				}else{
					
					for(int j = 0; j < selectedCandidates.size(); j++){
						selectedC = selectedCandidates.get(i).getActionCommand();
					}
				}
				int selectedIndex = panels.get(i).names.indexOf(selectedC);
				addVotes(race, selectedIndex, ID);
				System.out.println(race + " " + selectedC);
			}

			updateSummaryStatistics(user);
		}
	}

	/** Initializes server components */
	public void startServer() {
		/** Initialzies Server **/
		try {
			sock = new Socket("127.0.0.1", 50000);
			pwOut = new ObjectOutputStream(sock.getOutputStream());
			brIn = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void shutdown() {
		try {
			pwOut.writeObject("<shutdown>");
			System.exit(0);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * <<<SERVER CONNECTOIN <updateSummary> Takes in a Voter's ID | sends ID to
	 * server | updates summary statistics |
	 * 
	 * @param ID
	 *            - ID for voter
	 */
	public void updateSummaryStatistics(User ID) {
		for (String d : user.dataSet()) {
			try {
				pwOut.writeObject("<updateSummary>");
				pwOut.writeObject(d);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * <<<SERVER CONNECTOIN <vote> >>> saves user's candidate vote to server
	 **/
	public void addVotes(String race, int selectedIndex, String ID) {
		try {
			pwOut.writeObject("<vote>");
			pwOut.writeObject(race);
			pwOut.writeObject(selectedIndex);
			pwOut.writeObject(user.username);
			pwOut.writeObject(ID);
			confirm.setVisible(false);
		} catch (IOException j) {
			j.printStackTrace();
		}
	}

	/**
	 * <<<SERVER CONNECTOIN <saveballot> >>> saves completed ballot components
	 * to server
	 **/
	public void saveBallot() {

		try {
			pwOut.writeObject("<saveballot>");
			pwOut.writeObject(panels);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <<<SERVER CONNECTOIN ADDRACE>>> sends <addRace> command to server
	 * ultimate goald | update the candidates voting array |
	 **/
	
	
	public void addRace(RacePanel p, boolean mv) {

		try {
			pwOut.writeObject("<addRace>");
			pwOut.writeObject(p);
			pwOut.writeObject(mv);

		} catch (IOException j) {
			j.printStackTrace();
		}
	}

	public static void main(String args[]) {

	}

}
