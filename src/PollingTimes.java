import java.awt.Component;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


public class PollingTimes extends JPanel implements ActionListener {
	
	Date day;
	
	private JDatePickerImpl datePicker;

	
	public PollingTimes(){
		
		setLayout(new FlowLayout());
		
        
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        UtilDateModel model = new UtilDateModel();
 
        model.setSelected(true);
        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
 
        this.datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
        
        add(datePicker);
        
       /* JButton buttonOK = new JButton("OK");
		buttonOK.addActionListener(this);
        add(buttonOK);*/
        
        setSize(200,200);
            
	}
	
	public Date getDay(){
		
		return day;
	}
	
	public static void timeGUI(){
		JFrame frame = new JFrame("Polling Time");
		frame.setSize(700, 500);
		PollingTimes c = new PollingTimes();
		
		frame.add(c);
		
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	private static class DateLabelFormatter extends AbstractFormatter {

	    private String datePattern = "MM/dd/yyyy";
	    private SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

	    @Override
	    public Object stringToValue(String text) throws ParseException {
	        
	    	return dateFormatter.parseObject(text);
	    
	    }

	    @Override
	    public String valueToString(Object value) throws ParseException {
	        if (value != null) {
	            Calendar cal = (Calendar) value;
	            return dateFormatter.format(cal.getTime());
	        }

	        return "";
	    }

	}

    public static void main(String[] args) {	
    	
    	SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				timeGUI();
			}
		});

		
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals("confirm")){
			this.day = (Date) datePicker.getModel().getValue();
			
		}
		
	}
}