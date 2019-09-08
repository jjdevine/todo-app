package toDo.gui.alert;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import toDo.alert.*;
import toDo.data.*;
import toDo.exceptions.DateParseException;
import toDo.gui.customComponents.DateInput;
import toDo.gui.customComponents.ToDoHolder;
import toDo.interfaces.Alertable;
import toDo.interfaces.ToDoDisplayer;

public class PriorityAlertConfigure extends JFrame implements ActionListener
{

	private static final long serialVersionUID = 2362388364416995390L;
	private int sWidth = 700, sHeight = 590;
	private JPanel panelHeader, panelTop, panelMid, panelBottom, panelButtons;
	private JPanel panelTopLeft, panelTopRight, panelMidLeft, panelMidRight;
	private JPanel panelBottomLeft, panelBottomRight, panelCheckBox;
	private JLabel lHeader, lChange, lPriority, lAt;
	private JList lToDos;
	private JComboBox comboPriorities;
	private DateInput dateInput;
	private JCheckBox checkNotify;
	private JButton bConfirm, bCancel;
	private ToDoDisplayer source;
	private Alertable alertable;
	private List<ToDoItem> listToDos = new ArrayList<ToDoItem>();
	private DefaultListModel dlm = new DefaultListModel();
	private String[] priorities = {"Urgent", "High", "Medium", "Low"}; 
	
	public PriorityAlertConfigure(ToDoDisplayer source, Alertable alertable)
	{
		super("Create a Priority Alert");	//form heading
		//create container to place components in:
		Container container = getContentPane();
		container.setLayout(new FlowLayout());	//set flow layout
		
		this.source = source;
		this.alertable = alertable;
		
		/*
		 * create JPanels
		 */
		
		//initialise
		
		panelHeader = new JPanel();	
		panelTop = new JPanel();
		panelTopLeft = new JPanel();
		panelTopRight = new JPanel();
		panelMid = new JPanel();
		panelMidLeft = new JPanel();
		panelMidRight = new JPanel();
		panelBottom = new JPanel();
		panelBottomLeft = new JPanel();
		panelBottomRight = new JPanel();
		panelCheckBox = new JPanel();
		panelButtons = new JPanel();
		
		//set sizes
		
		panelHeader.setPreferredSize(new Dimension(sWidth - 20,50));
		
		panelTop.setPreferredSize(new Dimension(sWidth - 20,200));
		panelTopLeft.setPreferredSize(new Dimension(sWidth/2 - 20,190));
		panelTopRight.setPreferredSize(new Dimension(sWidth/2 - 20,190));
		
		panelMid.setPreferredSize(new Dimension(sWidth - 20,70));
		panelMidLeft.setPreferredSize(new Dimension(sWidth/2 - 20,60));
		panelMidRight.setPreferredSize(new Dimension(sWidth/2 - 20,60));
		
		panelBottom.setPreferredSize(new Dimension(sWidth - 20,95));
		panelBottomLeft.setPreferredSize(new Dimension(sWidth/2 - 20,85));
		panelBottomRight.setPreferredSize(new Dimension(sWidth/2 - 20,85));
		
		panelCheckBox.setPreferredSize(new Dimension(sWidth - 20, 50));
		
		panelButtons.setPreferredSize(new Dimension(sWidth - 20,50));
		
		//add borders
		
		panelHeader.setBorder(new LineBorder(Color.BLACK));
		panelTop.setBorder(new LineBorder(Color.BLACK));
		panelTopLeft.setBorder(new LineBorder(Color.RED));
		panelTopRight.setBorder(new LineBorder(Color.RED));
		panelMid.setBorder(new LineBorder(Color.BLACK));
		panelMidLeft.setBorder(new LineBorder(Color.RED));
		panelMidRight.setBorder(new LineBorder(Color.RED));
		panelBottom.setBorder(new LineBorder(Color.BLACK));
		panelBottomLeft.setBorder(new LineBorder(Color.RED));
		panelBottomRight.setBorder(new LineBorder(Color.RED));
		panelCheckBox.setBorder(new LineBorder(Color.BLACK));
		panelButtons.setBorder(new LineBorder(Color.BLACK));
		
		//add sub panels to their parent panels
		
		panelTop.add(panelTopLeft);
		panelTop.add(panelTopRight);
		
		panelMid.add(panelMidLeft);
		panelMid.add(panelMidRight);
		
		panelBottom.add(panelBottomLeft);
		panelBottom.add(panelBottomRight);
		
		/*
		 * header panel
		 */
		
		lHeader = new JLabel("Create a Priority Alert");
		lHeader.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
		panelHeader.add(lHeader);
		
		/*
		 * top left panel
		 */
		
		lChange = new JLabel("Change this To Do : ");
		panelTopLeft.setLayout(new GridBagLayout());
		lChange.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		panelTopLeft.add(lChange);
		
		/*
		 * top right panel
		 */
		
		//get list of todos to display
		List<? extends Object> list = this.source.getFilteredSortedToDoList();
		
		for(Object o: list)
		{
			if(o instanceof ToDoHolder)
			{
				listToDos.add(((ToDoHolder)o).getToDoItem());
			}
		}
		
		for(ToDoItem item: listToDos)
		{
			dlm.addElement(item.getDescription());
		}
		
		lToDos = new JList();
		lToDos.setModel(dlm);
		//lToDos.setPreferredSize(new Dimension(sWidth/2 - 50,180));
		lToDos.setBorder(new LineBorder(Color.BLACK));
		JScrollPane jsp = new JScrollPane(lToDos);
		jsp.setPreferredSize(new Dimension(sWidth/2 - 50,180));
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panelTopRight.add(jsp);
		
		/*
		 * panel mid left
		 */
		
		lPriority = new JLabel("To this priority : ");
		panelMidLeft.setLayout(new GridBagLayout());
		lPriority.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		panelMidLeft.add(lPriority);
			
		/*
		 * panel mid right
		 */
		
		comboPriorities = new JComboBox(priorities);
		comboPriorities.setPreferredSize(new Dimension(100, 40));
		comboPriorities.setFont(new Font("Comic Sans MS", Font.BOLD, 14));
		panelMidRight.setLayout(new GridBagLayout());
		panelMidRight.add(comboPriorities);
		
		/*
		 * panel bottom left
		 */
		
		lAt = new JLabel("At this time : ");
		panelBottomLeft.setLayout(new GridBagLayout());
		lAt.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		panelBottomLeft.add(lAt);
		
		/*
		 * panel bottom right
		 */
		
		dateInput = new DateInput();
		dateInput.insertTodaysDate();
		panelBottomRight.add(dateInput.getComponent());
		
		/*
		 * panel checkbox
		 */
		
		checkNotify = new JCheckBox("Alert me when this change is made");
		checkNotify.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
		checkNotify.setSelected(false);
		panelCheckBox.setLayout(new GridBagLayout());
		panelCheckBox.add(checkNotify);
		
		/*
		 * panel buttons
		 */
		
		bConfirm = new JButton("Confirm");
		bCancel = new JButton("Cancel");
		
		Dimension dButton = new Dimension(sWidth/2 - 40, 40);
		
		bConfirm.setPreferredSize(dButton);
		bCancel.setPreferredSize(dButton);
		
		bConfirm.addActionListener(this);
		bCancel.addActionListener(this);
		
		panelButtons.add(bConfirm);
		panelButtons.add(bCancel);
		
		/*
		 * add panels to container
		 */
		
		container.add(panelHeader);
		container.add(panelTop);
		container.add(panelMid);
		container.add(panelBottom);
		container.add(panelCheckBox);
		container.add(panelButtons);
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();	//get screen resoloution
		setLocation((d.width-sWidth)/2, (d.height-sHeight)/2);	//centre form
		
		setSize(sWidth,sHeight);	//set form size
		setVisible(true);//display screen
	}

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == bConfirm)
		{
			//info needed:
			int toDoID;
			int priority = ToDoItem.PRIORITY_URGENT;//default, although should never be used
			Calendar calAlert = null;
			boolean notify;
			
			int selectedIndex = lToDos.getSelectedIndex();
			
			if(selectedIndex != -1)
			{
				toDoID = listToDos.get(selectedIndex).getId();
				String strPriority = comboPriorities.getSelectedItem().toString();
				
				if(strPriority.equals(priorities[0]))//urgent
				{
					priority = ToDoItem.PRIORITY_URGENT;
				}
				else if(strPriority.equals(priorities[1]))//high
				{
					priority = ToDoItem.PRIORITY_HIGH;
				}
				else if(strPriority.equals(priorities[2]))//medium
				{
					priority = ToDoItem.PRIORITY_MEDIUM;
				}
				else if(strPriority.equals(priorities[3]))//low
				{
					priority = ToDoItem.PRIORITY_LOW;	
				}
				
				boolean exceptionThrown = false;
				try
				{
					calAlert = dateInput.getDate();
				}
				catch(DateParseException ex)
				{
					exceptionThrown = true;
					JOptionPane.showMessageDialog(null, "The date you entered is invalid!", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
				if(calAlert != null)
				{
					notify = checkNotify.isSelected();
					
					PriorityAlert a = new PriorityAlert();
					
					a.setToDoID(toDoID);
					a.setPriority(priority);
					a.setAlertTime(calAlert);
					a.setNotifier(notify);
					
					alertable.addAlert(a);
					
					dispose();
				}
				else
				{
					if(!exceptionThrown)
					{
						JOptionPane.showMessageDialog(null, "The date you entered is invalid!", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "Select a To Do to alert on!", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(e.getSource() == bCancel)
		{
			dispose();
		}
		
	}
	
}
