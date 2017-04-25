import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

/**
 * This class is the remove Election Commissioner interface and 
 * rewrites to the API the changes made
 * @author CodeFather
 *
 */
public class RemoveGUI extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;

	ArrayList<String> APIList = new ArrayList<String>();
	CurrentElection currentElection;
	
	JPanel panelMain = new JPanel();
	GroupLayout layout = new GroupLayout(panelMain);
	JPanel pnlNewComm = new JPanel();
	JLabel lcurrent;
	JLabel lnewComm;
	JTextField txtcurrent;
	JTextField txtnew;
	JButton finish;

	/**Server Stuff**/
	ObjectInputStream brIn;
	ObjectOutputStream pwOut;	
	Socket sock;


	RemoveGUI(CurrentElection currentElection){
		startServer();
		this.currentElection = currentElection;

		lcurrent = new JLabel("Current Election Commissioner ID");
		lnewComm = new JLabel("Replacement Election Commissioner ID");
		finish = new JButton("Finish");
		finish.setActionCommand("finish");
		finish.addActionListener(this);
		txtcurrent = new JTextField(20);
		txtnew = new JTextField(20);

		pnlNewComm.add(lcurrent);
		pnlNewComm.add(txtcurrent);
		pnlNewComm.add(lnewComm);
		pnlNewComm.add(txtnew);
		pnlNewComm.add(finish);
		pnlNewComm.setBorder(BorderFactory.createLineBorder(Color.WHITE));


		/**Default values for Main Panel | Color | Size | Icon | Title | **/
		panelMain.setBackground(MyColors.deepBlue);
		this.setSize(500,200);
		this.setIconImage(MyImages.codeFather.getImage());
		this.setTitle("MyVote");
		this.getContentPane().add(panelMain);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.getRootPane().setDefaultButton(finish);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lcurrent)
						.addComponent(txtcurrent))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lnewComm)
						.addComponent(txtnew))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(finish))
				);
		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lcurrent)
						.addComponent(lnewComm))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(txtcurrent)
						.addComponent(txtnew)
						.addComponent(finish))     		
				);


		/**Centers GUI to center of screen**/
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
		setLocation(x, y);
		setVisible(true);
	}


	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("finish")){			
			replaceAPI();
			this.setVisible(false);
			try {
				copyAPI();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void copyAPI() throws IOException{
		Files.copy(Paths.get("API2.txt"), new FileOutputStream("API.txt"));
	}

	public void replaceAPI(){
		String currentCommissioner = txtcurrent.getText();
		String newCommissioner = txtnew.getText();
		String currentElection = "";

		char alphabet[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
		String random = new String();
		int num = 0; 
		Random r = new Random();
		BufferedWriter bw = null, bw2 = null;
		FileWriter fw = null,  fw2 = null;
		String line = null;
		int counter = 0; 
		boolean place = false;
		try{
			File file = new File("API.txt");
			String users[]; 
			String userSearch = "";
			BufferedReader br = new BufferedReader(new FileReader(file));
			while((line = br.readLine()) != null && !place){
				users = line.split(" ");

				counter++;
				userSearch = users[1];
				if(users[1].equals(currentCommissioner)){
					currentElection = users[3];  
					place = true;
				}
			}
			if(place){
				
			int c = 0; 
			File file2 = new File("API2.txt");
			fw = new FileWriter(file2.getAbsoluteFile());
			bw = new BufferedWriter(fw);	
			String user[]; 
			
			BufferedReader br2 = new BufferedReader(new FileReader(file));
			while((line = br2.readLine()) != null && c < counter-1){
				user = line.split(" ");
			for(int i = 0; i < user.length; ++i){
				bw.write(user[i] + " ");
			}
			if(c == counter -2){
				
			}else bw.write("\r\n");			//windows carriage return
			c++;
			}
			//writes to API as EC username password ElectionName
			for(int i = 0; i < 10; ++i){
				num = r.nextInt(alphabet.length);
				random += alphabet[num]; 
			}

			bw.write("\nEC " + newCommissioner + " " + random + " " + currentElection);
			
			/**Reset Current Election Commissioner and API**/
			this.currentElection.replaceCommissioner(newCommissioner);
			addEC(newCommissioner, random, currentElection);
			new API().serializeAPI();
			
			fw2 = new FileWriter(file2.getAbsoluteFile(), true);
			bw2 = new BufferedWriter(fw2);
			
			br2.readLine();
			
			while((line = br2.readLine()) != null){
				user = line.split(" ");	
				for(int i = 0; i < user.length; ++i){
					bw.write(user[i] + " ");
				}
				bw.write("\r\n"); 		//windows carriage return
				
				bw.flush();

			}
			}
			else
			{
				
				JOptionPane.showMessageDialog(new JFrame(),userSearch + " Was not Found...Sorry...",": ( ERROR, ERROR, ERROR! : (",
						JOptionPane.PLAIN_MESSAGE,MyImages.yellowFrown);
				System.out.println("Election Commissioner Not Found...");
				this.setVisible(true);
			}
		} catch(IOException e){
			e.printStackTrace();

		} finally{
			try{
				if(bw != null)
					bw.close();
				if(fw != null)
					fw.close();
			}catch(IOException e2){
				e2.printStackTrace();
			}
		}
		
	}
	public void addEC(String username, String password, String electionName){
		try
		{
			pwOut.writeObject("<saveEC>");
			pwOut.writeObject(new ElectionCommissioner(username, password, electionName));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	public void shutdown(){
		try {
			pwOut.writeObject("<remove>");
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
