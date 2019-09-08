package toDo.gui.archive;

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

import toDo.gui.customComponents.ToDoArchivedHolder;
import toDo.interfaces.ToDoDisplayer;

public class DeleteArchivedToDo extends JFrame implements ActionListener
{
	private static final long serialVersionUID = -8656961303855053358L;
	private int sWidth = 400, sHeight = 450;
	private JPanel panelHeader, panelMain;
	private JLabel lHeader;
	private JList listToDos;
	private JButton bDelete, bClose;
	private List<ToDoArchivedHolder> list;
	private DefaultListModel dlm;
	private ToDoDisplayer main;
	
	public DeleteArchivedToDo(List<ToDoArchivedHolder> newList, ToDoDisplayer m)
	{
		super("Delete Archived To Do Items");
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
	    
	    lHeader = new JLabel("Delete To Dos");
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
		
		bDelete = new JButton("Delete");
		bClose = new JButton("Close");
		
		bDelete.addActionListener(this);
		bClose.addActionListener(this);
		
		bDelete.setPreferredSize(new Dimension(170,25));
		bClose.setPreferredSize(new Dimension(170,25));
		
		panelMain.add(jsp);
		panelMain.add(bDelete);
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
	
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == bDelete)
		{
			int index = listToDos.getSelectedIndex();
			
			if (index != -1)
			{
				ToDoArchivedHolder selectedHolder = list.get(index);
				
				int optionPicked = JOptionPane.showConfirmDialog(this, "Really Delete " + selectedHolder.getDescription() + "?", "Confirm Delete?", JOptionPane.YES_NO_OPTION);
				
				if (optionPicked == JOptionPane.YES_OPTION)
				{
					//list.remove(index);
					//main.refreshToDoDisplay();
					
					main.deleteToDoItem(list.get(index).getToDoItem());
					
					List toDoList = main.getFilteredSortedToDoList();
					/*
					 * need to make sure the reference here is the same as in 
					 * main when main refreshes it could create a new list so 
					 * we need to obtain the reference to that list after refresh
					 */
						
					list = toDoList;
					
					//refresh this form
					dlm = createDLM();
					listToDos.setModel(dlm);
				}
			}
			else
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
	public DefaultListModel createDLM()
	{
		DefaultListModel dlm = new DefaultListModel();
		
		Iterator<ToDoArchivedHolder> i = list.iterator();
		
		while(i.hasNext())
		{
			ToDoArchivedHolder holder = (ToDoArchivedHolder)i.next();
			
			dlm.addElement(holder.getDescription());
		}
		
		return dlm;
	}
	

}