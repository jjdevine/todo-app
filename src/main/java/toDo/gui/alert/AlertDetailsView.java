package toDo.gui.alert;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import toDo.data.*;
import toDo.alert.*;

public class AlertDetailsView extends JFrame implements ActionListener 
{

	private static final long serialVersionUID = 3452052001512335853L;

	private int sWidth = 300, sHeight = 290;
	private JTextArea textArea;
	private JButton bOK;
	
	public AlertDetailsView(Alert a, String toDoDescription)
	{
		super("Alert Details");	//form heading
		//create container to place components in:
		Container container = getContentPane();
		container.setLayout(new FlowLayout());	//set flow layout
		
		/*
		 * create components
		 */
		
		textArea = new JTextArea(createText(a, toDoDescription));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		
		bOK = new JButton("OK");
		bOK.setPreferredSize(new Dimension(280, 30));
		bOK.addActionListener(this);
		
		/*
		 * add components to container
		 */
		
		JScrollPane jsp = new JScrollPane(textArea);
		jsp.setBorder(new LineBorder(Color.BLACK));
		jsp.setPreferredSize(new Dimension(280,200));
		
		container.add(jsp);
		container.add(bOK);
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();	//get screen resoloution
		setLocation((d.width-sWidth)/2, (d.height-sHeight)/2);	//centre form
		
		setSize(sWidth,sHeight);	//set form size
		setVisible(true);//display screen	
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == bOK)
		{
			dispose();
		}
	}
	
	/**
	 * takes an alert and create a detailed string that the user can 
	 * read to describe the alert. 
	 */
	public String createText(Alert a, String toDoDescription)
	{
		StringBuilder text = new StringBuilder("");
		
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT);
		String dateTimeString = df.format(new Date(a.getAlertTime().getTimeInMillis()));
		
		if(a instanceof PriorityAlert)
		{
			PriorityAlert pa = (PriorityAlert)a;
			
			text.append("This alert will set the priority of the To Do '");
			text.append(toDoDescription + "'");
			text.append(" to " + ToDoItem.priorityAsString(pa.getPriority()));
			text.append(" on " + dateTimeString + ".");
			
			text.append("\n\n");
			if(pa.isNotifier())
			{
				text.append("You will be notified when this change occurs.");
			}
			else
			{
				text.append("You will not be notified when this change occurs.");
			}
		}
		else if(a instanceof MessageAlert)
		{
			text.append("This alert will generate a message on ");
			
			text.append(dateTimeString);
			text.append(" in relation to the To Do '" + toDoDescription + "'.");
			
			text.append("\n\n");
			text.append("The message generated will be as follows:");
			text.append("\n\n");
			text.append(((MessageAlert)a).getMessage());
		}
		
		return text.toString();
	}

}
