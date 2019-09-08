package toDo.gui.customComponents;

import java.awt.Color;
import java.awt.Dimension;

import toDo.data.ToDoItem;
import toDo.utilities.*;
import javax.swing.*;

/**
 * purpose of the ToDoHolder class is to create a Swing based representation
 * of a ToDoItem object. the most important method to other classes is 
 * the one that returns a JPanel Object, this JPanel is the graphical
 * representation of the current ToDoItem
 * 
 * @author jdevine5
 *
 */
public class ToDoHolder 
{

	private ToDoItem toDoItem;
	private JPanel panel;
	private JButton bPriority, bLog, bCompleted;
	private JTextField tDescription, tCreateDate, tLastModifiedDate;
	private final int rowHeight = 30;
	public static final int LOG_AUDIT = 0;
	public static final int LOG_COMMENT = 1;

	public ToDoHolder(ToDoItem newToDoItem)
	{
		toDoItem = newToDoItem;
		
		/*
		 * create graphical components
		 */
		
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(770,rowHeight));
		
		bPriority = new JButton(ToDoItem.priorityAsString(toDoItem.getPriority()));
		bPriority.setPreferredSize(new Dimension(90,rowHeight-5));
		
		tDescription = new JTextField(toDoItem.getDescription() + "");
		tDescription.setPreferredSize(new Dimension(300,rowHeight-5));
		tDescription.setEditable(false);
		tDescription.setBackground(Color.WHITE);
		
		tCreateDate = new JTextField(ToDoUtilities.formatDateToDD_MM_YY_hh_mm(toDoItem.getCreateDate()));
		tCreateDate.setPreferredSize(new Dimension(90,rowHeight-5));
		tCreateDate.setEditable(false);
		tCreateDate.setBackground(Color.WHITE);
		
		tLastModifiedDate = new JTextField(ToDoUtilities.formatDateToDD_MM_YY_hh_mm(toDoItem.getLastModifiedDate()));
		tLastModifiedDate.setPreferredSize(new Dimension(90,rowHeight-5));
		tLastModifiedDate.setEditable(false);
		tLastModifiedDate.setBackground(Color.WHITE);
		
		bLog = new JButton("Log");
		bLog.setPreferredSize(new Dimension(60,rowHeight-5));
		
		bCompleted = new JButton("Completed?");
		bCompleted.setPreferredSize(new Dimension(110,rowHeight-5));
		
		colourIfUrgent();
		
		/*
		 * add items to panel
		 */
		
		panel.add(bPriority);
		panel.add(tDescription);
		panel.add(tCreateDate);
		panel.add(tLastModifiedDate);
		panel.add(bLog);
		panel.add(bCompleted);
		
	}
	
	public JPanel getAsJPanel()
	{
		return panel;
	}
	
	public JButton getBCompleted() {
		return bCompleted;
	}

	public JButton getBLog() {
		return bLog;
	}

	public JButton getBPriority() {
		return bPriority;
	}

	public ToDoItem getToDoItem() {
		return toDoItem;
	}
	
	public void updatePriority(int priority)
	{
		toDoItem.setPriority(priority);
		updatePriorityComponent();
		addToLog(LOG_AUDIT, "Priority set to : " + ToDoItem.priorityAsString(priority));
	}
	
	public void updateDescription(String desc)
	{
		toDoItem.setDescription(desc);
		addToLog(LOG_AUDIT, "Description set to : " + desc);
		tDescription.setText(desc);
	}
	
	public void updateLastModified()
	{
		tLastModifiedDate.setText(ToDoUtilities.formatDateToDD_MM_YY_hh_mm(toDoItem.getLastModifiedDate()));
	}
	
	public void markComplete(String comments)
	{
		toDoItem.setCompleted(true);
		toDoItem.setId(-1);//make id invalid to avoid duplication
		String text = "To do marked complete." +
					"\nComments : " + comments;

		addToLog(LOG_AUDIT,text);
	}
	
	private void updatePriorityComponent()
	{
		bPriority.setText(ToDoItem.priorityAsString(toDoItem.getPriority()));
		colourIfUrgent();
	}
	
	private void colourIfUrgent()
	{
		/*
		 * urgent items are shown red
		 */
		
		if (toDoItem.getPriority() == ToDoItem.PRIORITY_URGENT)
		{
			bPriority.setForeground(Color.RED);
			bLog.setForeground(Color.RED);
			bCompleted.setForeground(Color.RED);
		}
		else
		{
			bPriority.setForeground(Color.BLACK);
			bLog.setForeground(Color.BLACK);
			bCompleted.setForeground(Color.BLACK);
		}
	}
	
	public void addToLog(int submissionType, String text)
	{
		/*
		 * method logic contained within ToDoItem class
		 */
		toDoItem.addToLog(submissionType, text);
		
		/*
		 * last modified date will be changed as a consequence of the above
		 */
		updateLastModified();
		
	}
	
	public boolean isCompleted()
	{
		return toDoItem.isCompleted();
	}
	
	public String getDescription()
	{
		return toDoItem.getDescription();
	}
	
	public String getLog()
	{
		return toDoItem.getLog().toString();
	}
	
	public int getPriority()
	{
		return toDoItem.getPriority();
	}
}
