import java.awt.Color;   
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;

public class BallotPrompt extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	/**Component Declaration**/
    RacePanel p;
	JTextField race; 
	JTextField candidates;
	JLabel raceLabel;
	JLabel candLabel; 
	JButton confirm;
	JButton finish;
	Ballot b = new Ballot();
    
	/**Creates interface for inputting ballot
	 *|Takes in title of race
	 *|Names of candidates
	 *|Generages Ballot on given input**/
	
    BallotPrompt(){
    	/**Panel Declarations**/
        JPanel mainPanel = new JPanel();
        GroupLayout layout = new GroupLayout(mainPanel);	
        JPanel racePanel = new JPanel();
        JPanel canPanel = new JPanel();
        
        /**Initializes buttons**/
        confirm = new JButton("Add Race");
        confirm.setActionCommand("confirm");
        confirm.addActionListener(this);
        
        finish = new JButton("Finish");
        finish.setActionCommand("finish");
        finish.addActionListener(this);
        
        /**Set Defaults for main panel | allows enter key | top bar icon**/
        this.getRootPane().setDefaultButton(confirm);
        this.setIconImage(MyImages.codeFather.getImage());
        this.setTitle("Create Ballot");
        this.setSize(450, 200);
        this.getContentPane().add(mainPanel);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainPanel.setBackground(MyColors.deepBlue);
        
        /**Initializes label and textFields**/
        race = new JTextField(20);   
        candidates = new JTextField(20);
        raceLabel = new JLabel("Please list the race title.");
        candLabel = new JLabel("Please enter candidates in race");

        /**race components to race panel**/
        racePanel.add(raceLabel);
        racePanel.add(race);
        racePanel.setBackground(MyColors.deepBlue);
        racePanel.setBorder(new LineBorder(Color.white,3));
        
        /**candidate components to candidates panel**/
        canPanel.add(candLabel);
        canPanel.add(candidates);
        canPanel.setBackground(MyColors.beechyBlue);
        canPanel.setBorder(new LineBorder(Color.white,3));
        
        /**Adds components to main pannel**/
        layout.setHorizontalGroup(layout.createSequentialGroup()
  			  .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
  					  .addComponent(racePanel)
  					  .addComponent(canPanel)
  					  .addComponent(confirm)
  					  .addComponent(finish)
  					  ));
  	  
	 	layout.setVerticalGroup( layout.createParallelGroup()
	 	       .addGroup(layout.createSequentialGroup())  					 
 	                  .addComponent(racePanel)
  					  .addComponent(canPanel)
  					  .addComponent(confirm)
  					  .addComponent(finish)
 	                   );
	 	
        
        /**Centers GUI onto Screen**/
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((d.getWidth() - this.getWidth()) / 2);
        int y = (int) ((d.getHeight() - this.getHeight()) / 2);
        setLocation(x, y);
        this.setVisible(true);  
     
        
	}
    /**Action Commands | Checks if confirm has been pressed
     * |Generates ballot based off input for ballot
     * |Uses Race_Panel to compartmentalize race panel components
     **/
    public void actionPerformed(ActionEvent e){
   
    	if(e.getActionCommand().equals("confirm")){
    		String[] candArray = candidates.getText().split(",");
    		p = new RacePanel(new JPanel(), race.getText(), candArray);
    		b.addBallot(p);
    	}
    	if(e.getActionCommand().equals("finish"))
    		b.finishBallot();
    }

	public static void main(String[] args) {
		
       new BallotPrompt();
	}

}