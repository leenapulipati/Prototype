import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

public class StudentBallot {

	Ballot b;
	ArrayList<Boolean> mv;
	boolean multivote = false;
	HashMap<String, List<Candidate>> lp;
	
	/** Server Stuff **/
	ObjectInputStream brIn;
	ObjectOutputStream pwOut;
	Socket sock;

	/**
	 * @param user
	 *            Re-builds ballot for user to vote
	 */
	StudentBallot(User user) {
		startServer();

		b = new Ballot(user);
		b.setVisible(false);
		lp = new HashMap<String, List<Candidate>>();
		getPanels();

		String[] races = lp.keySet().toArray(new String[0]);
		List<Candidate>[] winners = lp.values().toArray(new List[0]);
		
 
		for (int i = 0; i < races.length; i++) {
			
			RacePanel p = new RacePanel(races[i], winners[i]);
			
			
			b.addBallot(p, false, mv.get(i));
			
		}

		b.finishBallot(false);
		b.setVisible(true);
		

	}

	public void getPanels() {

		try {
			pwOut.writeObject("<getVotes>");
			
			lp = (HashMap<String, List<Candidate>>) brIn.readObject();
			mv = (ArrayList<Boolean>) brIn.readObject(); 
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void startServer() {
		/** Initializes Server **/
		try {
			sock = new Socket("127.0.0.1", 50000);
			pwOut = new ObjectOutputStream(sock.getOutputStream());
			brIn = new ObjectInputStream(sock.getInputStream());
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
