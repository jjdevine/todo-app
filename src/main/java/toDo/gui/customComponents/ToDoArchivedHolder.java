package toDo.gui.customComponents;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Calendar;

import javax.swing.*;

import toDo.data.ToDoItem;
import toDo.utilities.ToDoUtilities;

/**
 * wrapper class for todoitem, provides a gui in the form of a JPanel
 */
public class ToDoArchivedHolder 
{
	private ToDoItem toDoItem;
	private JPanel panel;
	private JButton bLog, bOpen, bPriority;
	private JTextField tDescription, tCreateDate, tLastModifiedDate;
	private final int rowHeight = 30;
	public static final int LOG_AUDIT = 0;
	public static final int LOG_COMMENT = 1;
	
	public ToDoArchivedHolder(ToDoItem newItem)
	{
		toDoItem = newItem;
		
		/*
		 * create graphical components
		 * 
		 * (need to fit in a panel of height rowHeight and width 750
		 */
		
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(750,rowHeight));
		
		bPriority = new JButton(ToDoItem.priorityAsString(toDoItem.getPriority()));
		bPriority.setPreferredSize(new Dimension(80,rowHeight-5));
		bPriority.setEnabled(false);
	
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
		
		bOpen = new JButton("Re-Open?");
		bOpen.setPreferredSize(new Dimension(100,rowHeight-5));
		
		/*
		 * add components to panel
		 */
		
		panel.add(bPriority);
		panel.add(tDescription);
		panel.add(tCreateDate);
		panel.add(tLastModifiedDate);
		panel.add(bLog);
		panel.add(bOpen);
		
	}
	
	

	public JButton getBLog() {
		return bLog;
	}

	public void setBLog(JButton log) {
		bLog = log;
	}

	public JButton getBOpen() {
		return bOpen;
	}

	public void setBOpen(JButton open) {
		bOpen = open;
	}

	public JButton getBPriority() {
		return bPriority;
	}

	public void setBPriority(JButton priority) {
		bPriority = priority;
	}

	public JPanel getPanel() {
		return panel;
	}

	public void setPanel(JPanel panel) {
		this.panel = panel;
	}

	public JTextField getTCreateDate() {
		return tCreateDate;
	}

	public void setTCreateDate(JTextField createDate) {
		tCreateDate = createDate;
	}

	public JTextField getTDescription() {
		return tDescription;
	}

	public void setTDescription(JTextField description) {
		tDescription = description;
	}

	public JTextField getTLastModifiedDate() {
		return tLastModifiedDate;
	}

	public void setTLastModifiedDate(JTextField lastModifiedDate) {
		tLastModifiedDate = lastModifiedDate;
	}

	public ToDoItem getToDoItem() {
		return toDoItem;
	}

	public void setToDoItem(ToDoItem toDoItem) {
		this.toDoItem = toDoItem;
	}
	
	/*
	 * methods to update graphical components, for each of these the underlying
	 * ToDoItem must be updated and the local graphical components must be 
	 * updated
	 */
	public void updatePriority(int priority)
	{
		toDoItem.setPriority(priority);
		bPriority.setText(ToDoItem.priorityAsString(priority));
	}
	
	public void updateDescription(String desc)
	{
		toDoItem.setDescription(desc);
		tDescription.setText(desc);
	}
	
	public void updateCreateDate(Calendar c)
	{
		toDoItem.setCreateDate(c);
		tCreateDate.setText(ToDoUtilities.formatDateToDD_MM_YY_hh_mm(toDoItem.getCreateDate()));
	}	
	
	public void updateLastModified()
	{
		//don't need to update actual item because that is done when comments are logged
		tLastModifiedDate.setText(ToDoUtilities.formatDateToDD_MM_YY_hh_mm(toDoItem.getLastModifiedDate()));
	}
	
	public boolean isCompleted()
	{
		return toDoItem.isCompleted();
	}
	
	public String getDescription()
	{
		return toDoItem.getDescription();
	}
	
	public void markIncomplete()
	{
		toDoItem.setCompleted(false);
	}
	
	
}
