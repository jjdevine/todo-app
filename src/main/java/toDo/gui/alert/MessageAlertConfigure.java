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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.*;
import javax.swing.border.LineBorder;

import toDo.alert.MessageAlert;
import toDo.data.ToDoItem;
import toDo.exceptions.DateParseException;
import toDo.gui.customComponents.DateInput;
import toDo.gui.customComponents.ToDoHolder;
import toDo.interfaces.ToDoDisplayer;

public class MessageAlertConfigure extends JFrame implements ActionListener {

	private static final long serialVersionUID = 2362388987616995390L;
	private int sWidth = 700, sHeight = 555;
	private JPanel panelHeader, panelTop, panelMid, panelBottom, panelButtons;
	private JPanel panelTopLeft, panelTopRight, panelMidLeft, panelMidRight;
	private JPanel panelBottomLeft, panelBottomRight;
	private JLabel lHeader, lChange, lMessage, lAt;
	private JList lToDos;
	private JTextArea textMessage;
	private DateInput dateInput;
	private JButton bConfirm, bCancel;
	private ToDoDisplayer source;
	private List<ToDoItem> listToDos = new ArrayList<ToDoItem>();
	private DefaultListModel dlm = new DefaultListModel();
	
	public MessageAlertConfigure(ToDoDisplayer source)
	{
		super("Create a Message Alert");	//form heading
		//create container to place components in:
		Container container = getContentPane();
		container.setLayout(new FlowLayout());	//set flow layout
		
		this.source = source;
		
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
		panelButtons = new JPanel();
		
		//set sizes
		
		panelHeader.setPreferredSize(new Dimension(sWidth - 20,50));
		
		panelTop.setPreferredSize(new Dimension(sWidth - 20,200));
		panelTopLeft.setPreferredSize(new Dimension(sWidth/2 - 20,190));
		panelTopRight.setPreferredSize(new Dimension(sWidth/2 - 20,190));
		
		panelMid.setPreferredSize(new Dimension(sWidth - 20,90));
		panelMidLeft.setPreferredSize(new Dimension(sWidth/2 - 20,80));
		panelMidRight.setPreferredSize(new Dimension(sWidth/2 - 20,80));
		
		panelBottom.setPreferredSize(new Dimension(sWidth - 20,95));
		panelBottomLeft.setPreferredSize(new Dimension(sWidth/2 - 20,85));
		panelBottomRight.setPreferredSize(new Dimension(sWidth/2 - 20,85));
		
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
		
		lHeader = new JLabel("Create a Message Alert");
		lHeader.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
		panelHeader.add(lHeader);
		
		/*
		 * top left panel
		 */
		
		lChange = new JLabel("Alert on : ");
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
		
		lMessage = new JLabel("With Message : ");
		panelMidLeft.setLayout(new GridBagLayout());
		lMessage.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
		panelMidLeft.add(lMessage);
			
		/*
		 * panel mid right
		 */
		
		textMessage = new JTextArea();
		textMessage.setLineWrap(true);
		textMessage.setWrapStyleWord(true);
		
		JScrollPane jsp2 = new JScrollPane(textMessage);
		jsp2.setPreferredSize(new Dimension(sWidth/2 - 50, 70));
		
		//panelMidRight.setLayout(new GridBagLayout());
		panelMidRight.add(jsp2);
		
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
			String message;
			Calendar calAlert = null;
			
			int selectedIndex = lToDos.getSelectedIndex();
			
			if(selectedIndex != -1)
			{
				toDoID = listToDos.get(selectedIndex).getId();
				
				message = textMessage.getText();
				
				if(!message.trim().equals(""))
				{
					boolean exceptionThrown = false;
					
					try
					{
						calAlert = dateInput.getDate();
					}
					catch(DateParseException ex)
					{
						exceptionThrown = true;
						JOptionPane.showMessageDialog(null, "Enter a valid date!", "Error", JOptionPane.ERROR_MESSAGE);
					}
					
					if(calAlert != null)
					{
						MessageAlert a = new MessageAlert();
						
						a.setToDoID(toDoID);
						a.setMessage(message);
						a.setAlertTime(calAlert);
						a.setNotifier(true);
						
						AlertManager.getInstance().addAlert(a);
						
						dispose();
					}
					else
					{
						if(!exceptionThrown)
						{
							JOptionPane.showMessageDialog(null, "Enter a date!", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Enter a message to alert with!", "Error", JOptionPane.ERROR_MESSAGE);
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
