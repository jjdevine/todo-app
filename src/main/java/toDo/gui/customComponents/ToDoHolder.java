package toDo.gui.customComponents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import toDo.data.ToDoItem;
import toDo.data.ToDoSchedule;
import toDo.events.AutoEscalationCommand;
import toDo.events.ToDoEvent;
import toDo.events.ToDoObserver;
import toDo.utilities.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * purpose of the ToDoHolder class is to create a Swing based representation
 * of a ToDoItem object. the most important method to other classes is 
 * the one that returns a JPanel Object, this JPanel is the graphical
 * representation of the current ToDoItem
 * 
 * @author jdevine5
 *
 */
public class ToDoHolder implements ActionListener
{

	private ToDoItem toDoItem;
	private JPanel panel;
	private JButton bPriority, bLog, bCompleted, bContextMenu;
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
		panel.setPreferredSize(new Dimension(820,rowHeight));

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

		bContextMenu = new JButton("...");
		bContextMenu.setPreferredSize(new Dimension(40,rowHeight-5));
		
		colourIfUrgent();

		ToDoSchedule schedule = toDoItem.getSchedule();

		if(schedule != null) {
			bPriority.setBorder(new LineBorder(Color.BLUE, 2));
			bPriority.setToolTipText("Escalates to " + schedule.getTargetPriority() +
				" every " + schedule.getDayFrequency() + " day(s)." +
				"  Next escalation: " + DateFormat.getDateInstance().format(schedule.getNextDueDate().getTime()));
		}
		
		/*
		 * add items to panel
		 */
		
		panel.add(bPriority);
		panel.add(tDescription);
		panel.add(tCreateDate);
		panel.add(tLastModifiedDate);
		panel.add(bLog);
		panel.add(bCompleted);
		panel.add(bContextMenu);

		/*
		Add action listeners
		 */

		bPriority.addActionListener(this);
		bContextMenu.addActionListener(this);
		
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

	private List<ToDoObserver> observers = new ArrayList<>();
	public void addToDoObserver(ToDoObserver o) {
		observers.add(o);
	}
	public void removeToDoObserver(ToDoObserver o) {
		observers.remove(o);
	}

	public void fireEvent(ToDoEvent e) {
		observers.forEach(o -> o.onEvent(e));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == bContextMenu) {
			handleContextMenuClick();
		} else if(e.getSource() == bPriority) {
			handlePriorityButtonClick();
		}
	}

	private void handlePriorityButtonClick() {
		int selection = JOptionPane.showOptionDialog(
				bPriority,
				"New Priority",
				toDoItem.getDescription(),
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				new String[] {"Urgent", "High", "Medium", "Low"},
				"Urgent");

		switch (selection) {
			case 0:
				toDoItem.setPriority(ToDoItem.PRIORITY_URGENT);
				break;
			case 1:
				toDoItem.setPriority(ToDoItem.PRIORITY_HIGH);
				break;
			case 2:
				toDoItem.setPriority(ToDoItem.PRIORITY_MEDIUM);
				break;
			case 3:
				toDoItem.setPriority(ToDoItem.PRIORITY_LOW);
				break;
		}

		Global.refreshView();
	}

	private void handleContextMenuClick() {
		String selection = (String)JOptionPane.showInputDialog(
				bContextMenu,
				"What do you want to do?",
				toDoItem.getDescription(),
				JOptionPane.QUESTION_MESSAGE,
				null,
				new String[] {"Configure Auto Escalation", "Cancel Auto Escalation"},
				"Configure Auto Escalation");

		if(selection == null) {
			return;
		}

		switch (selection) {
			case "Configure Auto Escalation":
				processConfigureAutoEscalationRequest();
				break;
			case "Cancel Auto Escalation":
				processCancelAutoEscalationRequest();
				break;
		}
	}

	private void processConfigureAutoEscalationRequest() {
		ToDoSchedule.TargetPriority typeSelection = (ToDoSchedule.TargetPriority)JOptionPane.showInputDialog(
				bContextMenu,
				"What type of auto escalation",
				toDoItem.getDescription(),
				JOptionPane.QUESTION_MESSAGE,
				null,
				ToDoSchedule.TargetPriority.values(),
				ToDoSchedule.TargetPriority.NEXT_PRIORITY);

		if(typeSelection == null) {
			return;
		}

		String frequency = JOptionPane.showInputDialog(bContextMenu, "Escalate how often (days)?", 7);

		if(frequency == null) {
			return;
		}

		int intFrequency = 0;
		try {
			intFrequency = Integer.parseInt(frequency);
			if(intFrequency < 1) {
				throw new NumberFormatException();
			}
		} catch (NumberFormatException ex) {
			showContextError(frequency + " is not a valid frequency");
			return;
		}

		int confirmSelection = JOptionPane.showConfirmDialog(bContextMenu, "Notify when escalation occurs?", toDoItem.getDescription(), JOptionPane.YES_NO_OPTION);

		AutoEscalationCommand command = new AutoEscalationCommand(
				typeSelection,
				intFrequency,
				confirmSelection == JOptionPane.YES_OPTION );

		ToDoEvent event = new ToDoEvent(ToDoEvent.Type.CONFIGURE_AUTO_ESCALATION, command, toDoItem);
		fireEvent(event);
	}

	private void processCancelAutoEscalationRequest() {
		fireEvent(new ToDoEvent(ToDoEvent.Type.CANCEL_AUTO_ESCALATION, null, toDoItem));
	}

	private void showContextError(String message) {
		JOptionPane.showMessageDialog(bContextMenu, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
}
