import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//changing
public class CurrentElection extends JFrame implements ActionListener{

	JLabel lblElectionName;
	JLabel lblCommissionerID;
	JLabel lblCandidates;
	JButton done;
	JButton back;
	HSOInterface election;

	CurrentElection(HSOInterface HSO){

		election = HSO;
		JPanel pnlRadios = new JPanel(new GridLayout(3,1));
		JPanel panelMain = new JPanel();
		GroupLayout layout = new GroupLayout(panelMain);
		JPanel panelElection = new JPanel();
		JPanel panelCommissioner = new JPanel();
		JPanel panelCandidates = new JPanel();
		JRadioButton radRecount = new JRadioButton("Recount Results");
		JRadioButton radDisqualify = new JRadioButton("Disqualify Voter");
		JRadioButton radCertify = new JRadioButton("Certify Election");
		JRadioButton radDisplay = new JRadioButton("Display Election");
		JRadioButton radRemove = new JRadioButton("Remove Election Commissioner");
		
		lblElectionName = new JLabel("Election Name:  " + election.getElection());
		lblCommissionerID = new JLabel("Election Commissioner:  " + election.getCommissioner());
		
		back = new JButton("Back to Elections");
		back.setActionCommand("back");
		back.addActionListener(this);
		
		done = new JButton("Done");
		done.setActionCommand("done");
		done.addActionListener(this);
		
		radRecount.setActionCommand("Recount");
		radRecount.addActionListener(this);

		radDisqualify.setActionCommand("Disqualify");
		radDisqualify.addActionListener(this);

		radCertify.setActionCommand("Certify");
		radCertify.addActionListener(this);

		radDisplay.setActionCommand("Display");
		radDisplay.addActionListener(this);

		radRemove.setActionCommand("Remove");
		radRemove.addActionListener(this);
		
		pnlRadios.add(radDisqualify);
		pnlRadios.add(radRecount);
		pnlRadios.add(radCertify);
		pnlRadios.add(radDisplay);
		pnlRadios.add(radRemove);
		
		/**Set Defaults for main panel | allows enter key | top bar icon**/
		this.getRootPane().setDefaultButton(done);
		this.setIconImage(MyImages.codeFather.getImage());
		this.setTitle("Current Election");
		this.setSize(450, 200);
		this.getContentPane().add(panelMain);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		panelMain.setBackground(MyColors.deepBlue);
		
		JPanel random = new JPanel();
		random.add(back);
		random.add(done);
		panelElection.add(lblElectionName);
		panelCommissioner.add(lblCommissionerID);
		
		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(panelElection)
						.addComponent(panelCommissioner))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(pnlRadios))
						.addComponent(random)
						
				);
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(panelElection)).addGap(50)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(panelCommissioner))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(pnlRadios))
						.addComponent(random)
				);


		/**Centers GUI onto Screen**/
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((d.getWidth() - this.getWidth()) / 2);
		int y = (int) ((d.getHeight() - this.getHeight()) / 2);
		setLocation(x, y);
		this.setVisible(true); 
	}

	public void actionPerformed(ActionEvent e){

		if(e.getActionCommand().equals("Recount")){
			new RecountGUI();
		}
		else if(e.getActionCommand().equals("Certify")){

			new CertifyResultsGUI();
		}

		else if(e.getActionCommand().equals("Display")){
			new DisplayGUI();
		}

		else if(e.getActionCommand().equals("Disqualify")){
			new DisqualifyGUI();
		}
		else if(e.getActionCommand().equals("Remove")){
			new RemoveGUI();	
		}
		
		if(e.getActionCommand().equals("back")){
			election.setVisible(true);
			this.setVisible(false);	
		}
		if(e.getActionCommand().equals("done"))
			this.setVisible(false);	
	}

}