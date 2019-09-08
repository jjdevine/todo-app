package toDo.scheduled;

import toDo.alert.*;
import toDo.data.ToDoItem;
import toDo.gui.alert.AlertBox;
import toDo.interfaces.ToDoDisplayer;
import toDo.utilities.ToDoFileIO;
import toDo.utilities.ToDoUtilities;
import toDo.utilities.ToDoXMLDecoder;

import java.util.*;
import java.io.*;

public class AlertProcessor 
{
	private List<Alert> listAlerts = new ArrayList<Alert>();
	private File alertFile;
	private ToDoDisplayer displayer;
	/**
	 * checks the alerts available and actions those that have met or passed
	 * their alert times. 
	 */
	
	public AlertProcessor(File f, ToDoDisplayer displayer)
	{
		this.displayer = displayer;
		alertFile = f;
		refreshList();
	}
	
	/**
	 * process alerts by looping through list of active alerts and actioning
	 * those that have expired.
	 */
	public synchronized void action()
	{
		int count = 0;
		
		while(count<listAlerts.size())
		{
			Calendar calNow = Calendar.getInstance();
			Alert alert = listAlerts.get(count);
			/*
			 * if it is now at or later than the time the alert is required
			 * to run
			 */
			if(alert.getAlertTime().compareTo(calNow)<=0)
			{
				/*
				 * process the alert then remove from list and reflect this
				 * in the save file
				 */
				processAlert(alert);
				listAlerts.remove(count);
				saveAlertsToFile();
			}
			else //don't need to action this one just yet
			{
				count++;
			}
		}
				
	}
	
	/**
	 * perform the actual actions required for this alert
	 * @param alert
	 */
	public synchronized void processAlert(Alert alert)
	{
		ToDoItem item = ToDoUtilities.getToDoByID(alert.getToDoID(), displayer.getToDoItemList());
		boolean notify = alert.isNotifier();
		
		if(alert instanceof PriorityAlert)
		{
			PriorityAlert pAlert = (PriorityAlert)alert;
			int priority = pAlert.getPriority();
			item.setPriority(priority);
			item.addToLog(ToDoItem.LOG_ALERT, "Priority set to : " + ToDoItem.priorityAsString(priority));
			if(notify)
			{
				new AlertBox("To Do '" + item.getDescription() + "' changed to priority " + ToDoItem.priorityAsString(priority));
			}
			displayer.refreshToDoDisplay();
			
		}
		else if(alert instanceof MessageAlert)
		{
			MessageAlert mAlert = (MessageAlert)alert;
			String message = mAlert.getMessage();
			item.addToLog(ToDoItem.LOG_ALERT, message);
			if(notify)
			{
				new AlertBox("Message alert for To Do '" + item.getDescription() + "'.\n\n" + message);
			}
		}
	}
	
	/**
	 * write the current alert list to file 
	 */
	public synchronized void saveAlertsToFile()
	{
		ToDoFileIO.saveAlertsAsFile(alertFile, listAlerts);
	}
	
	/**
	 * Reloads the list of alerts from the hard disk file
	 */
	public synchronized void refreshList()
	{
		listAlerts = new ToDoXMLDecoder(ToDoFileIO.loadFile(alertFile)).decodeXMLasAlertList();
	}

	public synchronized List<Alert> getListAlerts() {
		return listAlerts;
	}
	
	public synchronized void removeAlert(Alert a)
	{
		listAlerts.remove(a);
		saveAlertsToFile();
	}
	
	/**
	 * remove all alerts that match the id provided
	 * @param id
	 */
	public synchronized void removeAllAlertsForID(int id)
	{
		for(int index = listAlerts.size()-1; index>=0; index--)
		{
			Alert a = listAlerts.get(index);
			if(a.getToDoID() == id)
			{
				removeAlert(a);
			}
		}
	}
}
