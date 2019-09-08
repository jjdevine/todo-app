package toDo.gui.main;

import java.awt.Color;
import toDo.data.*;
import toDo.gui.customComponents.ToDoHolder;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class NewToDo extends JFrame implements ActionListener
{

	private static final long serialVersionUID = -7948201759038685117L;
	private int sWidth = 500, sHeight = 530;
	private JPanel panelHeader, panelDesc, panelPriority, panelComments, panelButtons;
	private JLabel lHeader, lDesc, lPriority, lComments;
	private JTextField tDesc;
	private JComboBox cbPriority;
	private JTextArea taComments;
	private JButton bCreate, bCancel;
	private MainGui main; //need reference to this
	
	public NewToDo(MainGui m)
	{
		super("New To Do Item");	//form heading
		//create container to place components in:
		Container container = getContentPane();
		container.setLayout(new FlowLayout());	//set flow layout
		
		//save reference to main form
		main = m;
		
		panelHeader = new JPanel();
		panelDesc = new JPanel();
		panelPriority = new JPanel();
		panelComments = new JPanel();
		panelButtons = new JPanel();
		
		panelHeader.setPreferredSize(new Dimension(470,50));
		panelDesc.setPreferredSize(new Dimension(470,35));
		panelPriority.setPreferredSize(new Dimension(470,35));
		panelComments.setPreferredSize(new Dimension(470,300));
		panelButtons.setPreferredSize(new Dimension(470,35));
		
		/*panelHeader.setBorder(new LineBorder(Color.BLACK));
		panelDesc.setBorder(new LineBorder(Color.BLACK));
		panelPriority.setBorder(new LineBorder(Color.BLACK));
		panelComments.setBorder(new LineBorder(Color.BLACK));
		panelButtons.setBorder(new LineBorder(Color.BLACK));*/
		
		/*
		 * header panel
		 */
		
		lHeader = new JLabel("Create a New To Do");
		lHeader.setFont(new Font("Comic Sans MS" , Font.BOLD, 24));
		
		panelHeader.add(lHeader);
		
		/*
		 * desc panel
		 */
		
		lDesc = new JLabel("Description : ");
		tDesc = new JTextField();
		
		lDesc.setPreferredSize(new Dimension(90,25));
		tDesc.setPreferredSize(new Dimension(300,25));
		
		//key listener to restrict max chars
		tDesc.addKeyListener(new MaxCharKeyListener(tDesc,40));
		
		panelDesc.add(lDesc);
		panelDesc.add(tDesc);
		
		/*
		 * priority panel
		 */
		
		String[] options = {" Urgent ", " High ", " Medium ", " Low "};
		
		lPriority = new JLabel("Priority : ");
		cbPriority = new JComboBox(options);
		
		lPriority.setPreferredSize(new Dimension(60,25));
		cbPriority.setPreferredSize(new Dimension(100,25));
		
		panelPriority.add(lPriority);
		panelPriority.add(cbPriority);
		
		/*
		 * comments panel
		 */
		
		lComments = new JLabel("Comments : ");
		taComments = new JTextArea();
		
		lComments.setPreferredSize(new Dimension(100,25));
		//taComments.setPreferredSize(new Dimension(420,240));
		
		taComments.setBorder(new LineBorder(Color.BLACK));
		taComments.setLineWrap(true);
		taComments.setWrapStyleWord(true);
		
		JScrollPane jsp = new JScrollPane(taComments);
		jsp.setPreferredSize(new Dimension(420,240));
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		panelComments.add(lComments);
		panelComments.add(jsp);
		
		/*
		 * button panel
		 */
		
		bCreate = new JButton("Create this To Do");
		bCancel = new JButton("Cancel");
		
		bCreate.setPreferredSize(new Dimension(220,25));
		bCancel.setPreferredSize(new Dimension(220,25));
		
		bCreate.addActionListener(this);
		bCancel.addActionListener(this);
		
		panelButtons.add(bCreate);
		panelButtons.add(bCancel);
		
		/*
		 * add all items to container
		 */
		
		container.add(panelHeader);
		container.add(panelDesc);
		container.add(panelPriority);
		container.add(panelComments);
		container.add(panelButtons);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();	//get screen resoloution
		setLocation((d.width-sWidth)/2, (d.height-sHeight)/2);	//centre form
		
		setSize(sWidth,sHeight);	//set form size
		setVisible(true);//display screen
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == bCreate)
		{
			/*
			 * validate first
			 */
			
			String error = "";
			boolean errorFound = false;
			
			//get description
			String desc = tDesc.getText().trim();
			
			if (desc.length() < 1)
			{
				error = "Please enter a description";
				errorFound = true;
			}
			
			if (!errorFound)
			{
				int index = cbPriority.getSelectedIndex();
				int priority;
				
				switch(index)
				{
					case 0:
						priority = ToDoItem.PRIORITY_URGENT;
						break;
					case 1:
						priority = ToDoItem.PRIORITY_HIGH;
						break;
					case 2:
						priority = ToDoItem.PRIORITY_MEDIUM;
						break;
					case 3:
						priority = ToDoItem.PRIORITY_LOW;
						break;
					default:
						priority = -1;
				}
				
				if (priority != -1) //valid priority selected
				{
					ToDoItem item = new ToDoItem(priority, desc);
					ToDoHolder holder = new ToDoHolder(item);
					
					//create audit info
					String audit;
					audit = "To Do Created. " +
							"\nDescription : " + desc +
							"\nPriority : " + ToDoItem.priorityAsString(priority) +
							"\nComments : " + taComments.getText().trim();
					
					holder.addToLog(ToDoHolder.LOG_AUDIT, audit);
					
					main.addToDo(holder);

					dispose();
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Select a valid priority", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
			else //error
			{
				JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
		
		if (e.getSource() == bCancel)
		{
			dispose();
		}
	}
	
}

/**
 * class to stop people making excessively long descriptions
 * @author jdevine5
 *
 */
class MaxCharKeyListener implements KeyListener
{

	JTextField tf;
	int maxChars;
	
	MaxCharKeyListener(JTextField newTF, int maxChars)
	{
		tf = newTF;
		this.maxChars = maxChars;
	}
	
	public void keyPressed(KeyEvent arg0) {}

	public void keyReleased(KeyEvent arg0) {}

	public void keyTyped(KeyEvent arg0) 
	{
		if (tf.getText().length() >= maxChars)
		{
			tf.setText(tf.getText().substring(0,maxChars));
		}
	}
	
}


