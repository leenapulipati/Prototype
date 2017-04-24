import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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

import javax.swing.*;
import javax.swing.border.LineBorder;

public class DisqualifyGUI extends JFrame implements ActionListener{

	ObjectInputStream brIn;
	ObjectOutputStream pwOut;	
	Socket sock;
	JTextField txtID;
	
	JLabel vName;
		
	DisqualifyGUI(){
		startServer();
	JPanel panelMain = new JPanel();
	JPanel buttons = new JPanel();
	GroupLayout layout = new GroupLayout(panelMain);
		
	txtID = new JTextField(20);
	
	JLabel lName = new JLabel("ID Name");
	lName.setFont(new Font(lName.getFont().getName(), Font.BOLD, 15));
	vName = new JLabel("Please enter the details");
	vName.setForeground(Color.RED);
	vName.setVisible(false);
	vName.setFont(new Font(vName.getFont().getName(), Font.BOLD, 15));
	
	JLabel disqualifyOptioins = new JLabel("Disqualify Optiosn");
	disqualifyOptioins.setFont(new Font(disqualifyOptioins.getFont().getName(), Font.BOLD, 15));

	
	JButton btnDisqualify = new JButton("Disqualify");
	btnDisqualify.setActionCommand("disqualify");
	btnDisqualify.addActionListener(this);
	

	JButton btnRecount = new JButton("Recount");
	btnRecount.setActionCommand("recount");
	btnRecount.addActionListener(this);
	
        JButton btnFinish = new JButton("Finish");
        btnFinish.setActionCommand("finish");
        btnFinish.addActionListener(this);
        buttons.add(btnDisqualify);
        buttons.add(btnRecount);
        buttons.add(btnFinish);
	
	panelMain.setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
	

	layout.setHorizontalGroup(
           layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                	.addComponent(vName)
                	.addComponent(lName)
                    .addComponent(txtID)                    
                    .addComponent(disqualifyOptioins)
                    .addComponent(buttons))
                    );
        layout.setVerticalGroup(
            layout.createParallelGroup()
                .addGroup(layout.createSequentialGroup()
                	.addComponent(vName)
                	.addComponent(lName)
                    .addComponent(txtID)                    
                    .addComponent(disqualifyOptioins)
                    .addComponent(buttons))
        );
        
	        //tells java what to do when the class object closes
		panelMain.setBackground(MyColors.deepBlue);
		this.setSize(600,400);
		this.setIconImage(MyImages.codeFather.getImage());
		this.setTitle("Disqualify");
		this.getContentPane().add(panelMain);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        
        //get visible container and add panelMain to it
        //EVERYTHING has to be arranged and set before adding to ContentPane
        getContentPane().add(panelMain);

        //this centers the window in the screen
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        setLocation(x, y);
        
        //make sure you can actually see it, starts off false
        setVisible(true);  
	        
	}
	 public void actionPerformed(ActionEvent e) 
	 {
			/**Confirms completed submission
	    	 * exits server and system**/
	    	if(e.getActionCommand().equals("finish"))
	    	{
	    		this.setVisible(false);
	    	}
	    	
	    	else if(e.getActionCommand().equals("recount"))
	    	{
	    		new RecountGUI();
	    	}
	    	else if(e.getActionCommand().equals("disqualify"))
	    	{
	    		 if(txtID.getText().isEmpty())
	        		vName.setVisible(true);
	    		 
	    		 else
	        	   {
		        	vName.setVisible(false);
		    		String strIn= disqualifyVoter();
		    		JOptionPane.showMessageDialog(this,strIn,"Confirmation",JOptionPane.PLAIN_MESSAGE);
	        	   }
	    	}
		} 

	public String disqualifyVoter() 
	{
		try 
		{
		pwOut.writeObject("<disqualify>");
		pwOut.writeObject( txtID.getText());
		return  (String)brIn.readObject();		
			
		} catch (IOException | ClassNotFoundException e) {
		  e.printStackTrace();
		}
		return null;
	 	}
	 
	 /**Starts Server Components**/
	public void startServer()
	{
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
	 
	 public static void main(String[]args){
		 MyVoteServer server = new MyVoteServer(true);
		 server.start();
		 server.restore();
		 
		 new DisqualifyGUI();
	 }

	
}
