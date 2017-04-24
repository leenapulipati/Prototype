import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Election  implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	 String election_title;
	 Date pollStartDay;
	 Date pollEndDay;
	 Votes votes;
	 boolean certified;
	public Election(String title, Date start, Date end)
	{
		pollStartDay = start;
		pollEndDay = end;
		election_title = title;
		certified = false;
		votes = new Votes();

	}
	
	public String toString()
	{
		return election_title;
	}
}
