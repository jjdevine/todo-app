package toDo.utilities;

import java.util.*;

import toDo.alert.*;
import toDo.data.*;

public class ToDoXMLEncoder 
{
	private List<ToDoItem> itemList = new ArrayList<ToDoItem>();
	public static String ENCODED_GREATER_THAN = "###GREATER_THAN_SYMBOL###";
	public static String ENCODED_LESS_THAN = "###LESS_THAN_SYMBOL###";
	
	public ToDoXMLEncoder(List<ToDoItem> items)
	{
		itemList = items;
	}
	
	public ToDoXMLEncoder()
	{
	}
	
	public String encodeXMLToDoFile(boolean fullFile)
	{
		
		StringBuilder sb;
		if(fullFile)
		{
			sb = new StringBuilder("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		}
		else
		{
			sb = new StringBuilder("");
		}
		
		Iterator i = itemList.iterator();
		
		while (i.hasNext())
		{
			ToDoItem thisItem = (ToDoItem)i.next();
			
			/*
			 * get details
			 */
			
			int id = thisItem.getId();
			int priority = thisItem.getPriority();
			String description = thisItem.getDescription();
			description = description.replaceAll("<", ENCODED_LESS_THAN);
			description = description.replaceAll(">", ENCODED_GREATER_THAN);
			Calendar createDate = thisItem.getCreateDate();
			Calendar lastModifiedDate = thisItem.getLastModifiedDate();
			boolean completed = thisItem.isCompleted();
			ToDoSchedule schedule = thisItem.getSchedule();
			String log = thisItem.getLog().toString();
			log = log.replaceAll("<", ENCODED_LESS_THAN);
			log = log.replaceAll(">", ENCODED_GREATER_THAN);
			
			sb.append("<ToDoItem>\n");
			
			sb.append(blankSpaces(4)+"<ID>");
			sb.append(id);
			sb.append("</ID>\n");
			
			sb.append(blankSpaces(4)+"<Priority>");
			sb.append(ToDoItem.priorityAsString(priority));
			sb.append("</Priority>\n");
			
			sb.append(blankSpaces(4)+"<Description>");
			sb.append(description);
			sb.append("</Description>\n");
			
			sb.append(blankSpaces(4)+"<CreateDate>");
			sb.append(ToDoUtilities.formatDateToDD_MM_YYYY_hh_mm(createDate));
			sb.append("</CreateDate>\n");
			
			sb.append(blankSpaces(4)+"<LastModifiedDate>");
			sb.append(ToDoUtilities.formatDateToDD_MM_YYYY_hh_mm(lastModifiedDate));
			sb.append("</LastModifiedDate>\n");
			
			sb.append(blankSpaces(4)+"<Completed>");
			sb.append(completed);
			sb.append("</Completed>\n");

			if(schedule != null) {
				sb.append(schedule.toXml() + "\n");
			}
			
			sb.append(blankSpaces(4)+"<Log>");
			sb.append(log);
			sb.append("</Log>\n");
			
			sb.append("</ToDoItem>\n");
		}
		
		return sb.toString();
	}
	
	public String formatDate(Calendar c)
	{
		return c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.YEAR);
	}
	
	public String blankSpaces(int x)
	{
		StringBuilder sb = new StringBuilder();
		for(int count=0;count<x;count++)
		{
			sb.append(" ");
		}
		return sb.toString();
	}
	
	/**
	 * returns a string representing an xml file that stores information
	 * so that the list of alert objects can be recreated. 
	 * @param listAlerts
	 * @return
	 */
	public String encodeXMLAlertFile(List<Alert> listAlerts)
	{
		StringBuilder sb = new StringBuilder("");
		
		for(Alert a: listAlerts)
		{
			sb.append("<Alert>\n");
			
			if(a instanceof PriorityAlert) //specific encoding of priority alert
			{
				PriorityAlert pAlert = (PriorityAlert)a;
				
				sb.append(blankSpaces(4) + "<AlertType>Priority</AlertType>\n");
				
				sb.append(blankSpaces(4) + "<DateTime>");
				sb.append(ToDoUtilities.formatDateToDD_MM_YYYY_hh_mm(pAlert.getAlertTime()));
				sb.append("</DateTime>\n");
				
				sb.append(blankSpaces(4) + "<ToDoID>");
				sb.append(pAlert.getToDoID());
				sb.append("</ToDoID>\n");
				
				sb.append(blankSpaces(4) + "<Notify>");
				sb.append(pAlert.isNotifier());
				sb.append("</Notify>\n");
				
				sb.append(blankSpaces(4) + "<Priority>");
				sb.append(ToDoItem.priorityAsString(pAlert.getPriority()));
				sb.append("<Priority>\n");
			}
			else if(a instanceof MessageAlert)
			{
				MessageAlert mAlert = (MessageAlert)a;
				
				sb.append(blankSpaces(4) + "<AlertType>Message</AlertType>\n");
				
				sb.append(blankSpaces(4) + "<DateTime>");
				sb.append(ToDoUtilities.formatDateToDD_MM_YYYY_hh_mm(mAlert.getAlertTime()));
				sb.append("</DateTime>\n");
				
				sb.append(blankSpaces(4) + "<ToDoID>");
				sb.append(mAlert.getToDoID());
				sb.append("</ToDoID>\n");
				
				sb.append(blankSpaces(4) + "<Notify>");
				sb.append(mAlert.isNotifier());
				sb.append("</Notify>\n");
				
				sb.append(blankSpaces(4) + "<Message>");
				sb.append(mAlert.getMessage().replaceAll("<", ENCODED_LESS_THAN).replaceAll(">", ENCODED_GREATER_THAN));
				sb.append("</Message>\n");
			}
			sb.append("</Alert>\n");
		}
		
		return sb.toString();
	}
	
}
