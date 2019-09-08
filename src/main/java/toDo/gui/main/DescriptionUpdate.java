package toDo.gui.main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

import toDo.gui.customComponents.ToDoHolder;

public class DescriptionUpdate extends JFrame implements ActionListener
{
	private static final long serialVersionUID = -8656961303855053358L;
	private int sWidth = 400, sHeight = 450;
	private JPanel panelHeader, panelMain;
	private JLabel lHeader;
	private JList listToDos;
	private JButton bUpdate, bClose;
	private List<ToDoHolder> list;
	private DefaultListModel dlm;
	private MainGui main;
	
	public DescriptionUpdate(List<ToDoHolder> newList, MainGui m)
	{
		super("Update To Do Descriptions");
		Container container = getContentPane();
	    container.setLayout(new FlowLayout());	//set flow layout
	    
	    list = newList;
	    main = m;
	    
	    /*
	     * create panels
	     */
	    
	    panelHeader = new JPanel();
	    panelMain = new JPanel();
	    
	    panelHeader.setPreferredSize(new Dimension(370,50));
	    panelMain.setPreferredSize(new Dimension(370,350));
	    
	    panelHeader.setBorder(new LineBorder(Color.BLACK));
	    panelMain.setBorder(new LineBorder(Color.BLACK));
	    
	    /*
	     * header panel
	     */
	    
	    lHeader = new JLabel("Change To Do Description");
	    lHeader.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
	    
	    panelHeader.add(lHeader);
	    
	    /*
	     * main panel
	     */
	    
	    dlm = createDLM();
		listToDos = new JList(dlm);
		listToDos.setFixedCellWidth(350);
		listToDos.setVisibleRowCount(list.size());
		
		JScrollPane jsp = new JScrollPane(listToDos, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(350, 305));
		
		bUpdate = new JButton("Update");
		bClose = new JButton("Close");
		
		bUpdate.addActionListener(this);
		bClose.addActionListener(this);
		
		bUpdate.setPreferredSize(new Dimension(170,25));
		bClose.setPreferredSize(new Dimension(170,25));
		
		panelMain.add(jsp);
		panelMain.add(bUpdate);
		panelMain.add(bClose);
		
		/*
		 * add panels to container
		 */
		
		container.add(panelHeader);
		container.add(panelMain);
	    
	    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();	//get screen resoloution
	    setLocation((d.width-sWidth)/2, (d.height-sHeight)/2);
	    
	    setSize(sWidth,sHeight);	//set form size
		setVisible(true);		//make form visible	
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == bUpdate)
		{
			int index = listToDos.getSelectedIndex();
			
			
			if(index != -1)//selection made
			{
				ToDoHolder holder = list.get(index);
				//flag for if input is valid
				boolean exitOK = false;
				String input = holder.getDescription();
				
				while(!exitOK) //keep input box popping up unless cancelled or valid input
				{
					input = JOptionPane.showInputDialog(null,"Enter a new Description (50 char max)",input);	//ask user for name
					if (input == null) //cancel pressed
					{
						exitOK = true;
					}
					else if (input.trim().length() > 50)
					{
						JOptionPane.showMessageDialog(null, "Your description is too long (50 char max)!", "Error", JOptionPane.ERROR_MESSAGE);
					}
					else if(input.trim().length() < 1)
					{
						JOptionPane.showMessageDialog(null, "You must enter a description!", "Error", JOptionPane.ERROR_MESSAGE);
					}
					else //valid input
					{
						exitOK = true;
						holder.updateDescription(input.trim());
						main.refreshToDoDisplay();
						
						//refresh this form
						dlm = createDLM();
						listToDos.setModel(dlm);
					}
				}
				
			}
			else //no selection made
			{
				JOptionPane.showMessageDialog(null, "You need to select a description to update!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
		}
		if (e.getSource() == bClose)
		{
			dispose();
		}
		
	}
	
	/**
	 * create DefaultListModel of todo descriptions
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public DefaultListModel createDLM()
	{
		DefaultListModel dlm = new DefaultListModel();
		
		//synchronise lists
		list = main.getFilteredSortedToDoList();
		
		Iterator i = list.iterator();
		
		while(i.hasNext())
		{
			ToDoHolder holder = (ToDoHolder)i.next();
			
			dlm.addElement(holder.getDescription());
		}
		
		return dlm;
	}
	

}
