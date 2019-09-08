package toDo.gui.main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import toDo.data.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import toDo.gui.customComponents.ToDoHolder;

public class PriorityChange extends JFrame implements ActionListener
{

	private static final long serialVersionUID = -4769817495384496161L;
	private int sWidth = 500, sHeight = 280;
	private ToDoHolder holder;
	//need reference to main class to tell it to refresh
	private MainGui main;
	private JLabel lHeader;
	private JButton bUrgent, bHigh, bMedium, bLow, bCancel;
	private JPanel panelHeader, panelButtons;
	
	public PriorityChange(ToDoHolder newHolder, MainGui newMain)
	{
		super("Priority Change");	//form heading
		//create container to place components in:
		Container container = getContentPane();
		container.setLayout(new FlowLayout());	//set flow layout
		
		//store references
		main = newMain;
		holder = newHolder;
		
		panelHeader = new JPanel();
		panelButtons = new JPanel();
		
		panelHeader.setPreferredSize(new Dimension (470,50));
		panelButtons.setPreferredSize(new Dimension (470,175));
		
		panelHeader.setBorder(new LineBorder(Color.BLACK));
		panelButtons.setBorder(new LineBorder(Color.BLACK));
		
		/*
		 * add buttons
		 */
		
		bUrgent = new JButton("Set to Urgent");
		bHigh = new JButton("Set to High");
		bMedium = new JButton("Set to Medium");
		bLow= new JButton("Set to Low");
		bCancel = new JButton("Cancel");
		
		Dimension dButton = new Dimension(220,50);
		
		bUrgent.setPreferredSize(dButton);
		bHigh.setPreferredSize(dButton);
		bMedium.setPreferredSize(dButton);
		bLow.setPreferredSize(dButton);
		bCancel.setPreferredSize(dButton);
		
		bUrgent.addActionListener(this);
		bHigh.addActionListener(this);
		bMedium.addActionListener(this);
		bLow.addActionListener(this);
		bCancel.addActionListener(this);
		
		panelButtons.add(bUrgent);
		panelButtons.add(bHigh);
		panelButtons.add(bMedium);
		panelButtons.add(bLow);
		panelButtons.add(bCancel);
		
		
		//dont allow user to set priority to the one already set
		disableCurrentPriority();
		
		/*
		 * description could be long, so take a substring if exceeds certain
		 * length (for display purposes)
		 */
		
		String desc = holder.getToDoItem().getDescription();
		
		if(desc.length() > 20)
		{
			desc = desc.substring(0, 18) + "...";
		}
		
		lHeader = new JLabel ("Priority of \"" + desc + "\"");
		lHeader.setFont(new Font("Comic Sans MS" , Font.BOLD, 24));
		
		panelHeader.add(lHeader);
		
		/*
		 * add panels to form
		 */
		
		container.add(panelHeader);
		container.add(panelButtons);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();	//get screen resoloution
		setLocation((d.width-sWidth)/2, (d.height-sHeight)/2);	//centre form
		
		setSize(sWidth,sHeight);	//set form size
		setVisible(true);//display screen
	}
	
	
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == bUrgent)
		{
			holder.updatePriority((ToDoItem.PRIORITY_URGENT));
			main.refreshToDoDisplay();
			dispose();
		}
		
		if(e.getSource() == bHigh)
		{
			holder.updatePriority(ToDoItem.PRIORITY_HIGH);
			main.refreshToDoDisplay();
			dispose();
		}
		
		if(e.getSource() == bMedium)
		{
			holder.updatePriority(ToDoItem.PRIORITY_MEDIUM);
			main.refreshToDoDisplay();
			dispose();
		}
		
		if(e.getSource() == bLow)
		{
			holder.updatePriority(ToDoItem.PRIORITY_LOW);
			main.refreshToDoDisplay();
			dispose();
		}
		
		if(e.getSource() == bCancel)
		{
			dispose();
		}
		
	}
	
	/**
	 * disable the button that represents the current priority of the ToDoItem
	 *
	 */
	public void disableCurrentPriority()
	{
		int priority = holder.getToDoItem().getPriority();
		
		switch(priority)
		{
			case ToDoItem.PRIORITY_URGENT:
				bUrgent.setEnabled(false);
				break;
			case ToDoItem.PRIORITY_HIGH:
				bHigh.setEnabled(false);
				break;
			case ToDoItem.PRIORITY_MEDIUM:
				bMedium.setEnabled(false);
				break;
			case ToDoItem.PRIORITY_LOW:
				bLow.setEnabled(false);
				break;
		}
	}

}
