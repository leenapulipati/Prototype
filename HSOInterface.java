import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.awt.*;
import javax.swing.*;
import java.util.Random;

public class HSOInterface extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;

	ArrayList<String> contents = new ArrayList<String>();
	DefaultListModel<String> model = new DefaultListModel<String>();
	JList<String> list = new JList<String>(model);
	DefaultListCellRenderer renderer = (DefaultListCellRenderer)list.getCellRenderer();
	
	
	ArrayList<String> comm = new ArrayList<String>();

	JPanel panelMain = new JPanel();
	GroupLayout layout = new GroupLayout(panelMain);
	JPanel pnlRadios = new JPanel();
	JPanel pnlList = new JPanel();
	
	String u;
	JLabel lWelcome;

	ElectionPrompt election;
	JLabel lCurrentElections; 
	ArrayList<String> commissioner = new ArrayList<String>(); 
	JButton finish; 

	Election[] elections;
	Election[] commissioners;
	/**Server Stuff**/
	ObjectInputStream brIn;
	ObjectOutputStream pwOut;	
	Socket sock;


	HSOInterface() throws IOException{
		startServer();
		lCurrentElections = new JLabel("Or choose a current election");
		
		renderer.setHorizontalAlignment(JLabel.CENTER);
		
		String line = null;
		try{
			File file = new File("API.txt");

			String users[]; 

			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(file));
			while((line = br.readLine()) != null){
				users = line.split(" ");
				if(users[0].equals("HSO")){
					u = users[1];
				}
			}
		} catch(FileNotFoundException e){
			e.printStackTrace();
		}

		lWelcome = new JLabel("Welcome HSO: " + u + "!");
		lWelcome.setForeground(Color.WHITE);
		
		
		pnlList.setLayout(new BoxLayout(pnlList, BoxLayout.Y_AXIS));
		pnlList.add(lCurrentElections);
		pnlList.add(Box.createVerticalStrut(20));
		pnlList.add(list);
		pnlList.setAutoscrolls(true);

		finish = new JButton("Finish");
		finish.setActionCommand("finish");
		finish.addActionListener(this);

		pnlList.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		pnlList.setLayout(new BoxLayout(pnlList, BoxLayout.Y_AXIS));
		JRadioButton radNewElection = new JRadioButton("Create New Election");
		
		pnlList.setMaximumSize(new Dimension(200, 100));
		radNewElection.setActionCommand("NewElection");
		radNewElection.addActionListener(this);

		pnlRadios.add(radNewElection);

		pnlRadios.setBorder(BorderFactory.createLineBorder(Color.WHITE));

		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setVisibleRowCount(4);
		list.setVisible(true);
		pnlRadios.setLayout(new BoxLayout(pnlRadios, BoxLayout.Y_AXIS));

		panelMain.setBackground(MyColors.deepBlue);
		this.setSize(450,500);
		this.setIconImage(MyImages.codeFather.getImage());
		this.setTitle("HSO Interface");
		this.getContentPane().add(panelMain);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		panelMain.setLayout(layout);


		getList();
		initializeList();

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lWelcome))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(pnlRadios)
				//.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(pnlList)
						.addComponent(finish))
				);

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lWelcome).addGap(50))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(pnlRadios)).addGap(50)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(pnlList)).addGap(50)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(finish)
						));

		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
		setLocation(x, y);
		setVisible(true);

	}

	public void addList(String name){

		/**Retrieves Election Name and adds to current Election List**/
		contents.add(name);
		//comm.add(contents.indexOf(name), id);

		model.addElement(contents.get(contents.size()-1));

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(lWelcome))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(pnlRadios)
				//.addGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)
						.addComponent(pnlList)
						.addComponent(finish))

				);

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(lWelcome).addGap(50))
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(pnlRadios)).addGap(50)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(pnlList)).addGap(50)
				.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(finish))
				);

		panelMain.setBackground(MyColors.deepBlue);
		this.setSize(400,600);
		this.setIconImage(MyImages.codeFather.getImage());
		this.setTitle("MyVote");
		this.getContentPane().add(panelMain);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


		/**Centers GUI to center of screen**/
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
		setLocation(x, y);
		setVisible(true);

	}

	public void actionPerformed(ActionEvent e){
		
		if(e.getActionCommand().equals("NewElection")){

			/**Have election take in current election**/
			new ElectionPrompt(this);

			setVisible(false);
		}
		if(list.getSelectedValue() != null){
			String selectedE = list.getSelectedValue().toString();
			
			initializeElection(selectedE);
			new CurrentElection(this);
			this.setVisible(false);
		}

		if(e.getActionCommand().equals("finish"))
			this.setVisible(false);
	
	}


	public void getList()
	{
		try {
			pwOut.writeObject("<getElections>");
			elections = (Election[])brIn.readObject();
		} catch (IOException | ClassNotFoundException e) {

			e.printStackTrace();
		}
	}

	public void initializeList()
	{
		for(Election e: elections)
			this.addList(e.election_title);
	}

	public void initializeElection(String selectedE)
	{
		try {
			pwOut.writeObject("<selectedElection>");
			pwOut.writeObject(selectedE);

		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	public void addCommissioner(String id, String eTitle)
	{
		char alphabet[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
		String random = new String();
		int num = 0; 
		Random r = new Random();


		BufferedWriter bw = null;
		FileWriter fw = null;

		try{
			File file = new File("API.txt");

			fw = new FileWriter(file.getAbsoluteFile(), true);
			bw = new BufferedWriter(fw);
			
			//writes to API as EC username password ElectionName
			for(int i = 0; i < 10; ++i){
				num = r.nextInt(alphabet.length);
				
				random += alphabet[num]; 
			}
		
			bw.write("\nEC " + id + " " + random + " " + eTitle);
			bw.flush();
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
		//commissioner.add(id);
	}


	public String getElection(){
		return list.getSelectedValue().toString();	
	}

		public String getCommissioner(){
			String line = null;
			try{
				File file = new File("API.txt");

				String users[]; 

				BufferedReader br = new BufferedReader(new FileReader(file));
				while((line = br.readLine()) != null){
					users = line.split(" ");
					if(users[3].equals(list.getSelectedValue())){
						u = users[1];
					}

				}
			} catch(IOException e){
				e.printStackTrace();
			}
			
			return u;
			//int indexes[] = list.getSelectedIndices();
		//return commissioner.get(list.getSelectedIndex());	
	}


	public void shutdown(){
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



	public static void main(String args[])
	{
		try {
			new HSOInterface();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
