package toDo.utilities;

import java.util.*;
import toDo.alert.*;
import toDo.data.*;

enum AlertType{
	MESSAGE("MESSAGE"), 
	PRIORITY("PRIORITY");
	
	String asString;
	
	AlertType(String s)
	{
		asString = s;
	}
	
} 

public class ToDoXMLDecoder 
{
	private String xmlText; 
	public final String ALERT_TAG = "Alert";
	public final String ALERT_TYPE_TAG = "AlertType";
	public final String ALERT_DATETIME_TAG = "DateTime";
	public final String ALERT_TODO_ID_TAG = "ToDoID";
	public final String ALERT_NOTIFY_TAG = "Notify";
	public final String ALERT_PRIORITY_TAG = "Priority";
	public final String ALERT_MESSAGE_TAG = "Message";
	
	public ToDoXMLDecoder(String xmlText)
	{
		this.xmlText = xmlText;
	}
	
	public ToDoXMLDecoder()
	{

	}
	
	/**
	 * reads the tag type of the first tag in a supplied string
	 * @param xmlLine
	 * @return
	 */
	public String getFirstTagValue(String xmlLine)
	{
		int openIndex = xmlLine.indexOf('<');
		int closeIndex = xmlLine.indexOf('>');
		
		if (openIndex == -1 || closeIndex == -1)
		{
			return "";
		}
		
		return xmlLine.substring(openIndex+1, closeIndex);
	}
	
	/**
	 * gets the value associated with the tags on this line. This method assumes
	 * there is only one value tag on the supplied line, and both opening and closing
	 * tags are on this line.
	 * @param xmlLine
	 * @return
	 */
	public String getTagValue(String xmlLine)
	{
		int firstTagCloseIndex = xmlLine.indexOf('>');
		int closingTagOpenIndex = xmlLine.lastIndexOf('<');
		
		if (firstTagCloseIndex == -1 || closingTagOpenIndex == -1)
		{
			return "";
		}
		
		return xmlLine.substring(firstTagCloseIndex+1, closingTagOpenIndex);
		
	}
	
	/**
	 * returns the index of the start of the tag, if tag doesn't exist
	 * -1 is returned
	 * @param xmlLine
	 * @return
	 */
	public int indexTagOnLine(String tag, String xmlLine)
	{
		/*
		 * first check if tag is on this line
		 */
		
		String regex = "<" + tag + ">";

		for(int x=0; x<xmlLine.length()-regex.length()+1; x++)
		{
			if(xmlLine.substring(x, x+regex.length()).equalsIgnoreCase(regex))
			{
				return x; //index of first "<"
			}
		}
    	
	    return -1;
	}
	
	/**
	 * returns the xml as a list of ToDoItems
	 * @return
	 */
	public List<ToDoItem> decodeXMLasToDoItemList()
	{
		List<ToDoItem> newList = new ArrayList<ToDoItem>();
		
		String[] arrFile = xmlText.split("\n");//split file by lines
		
		int lineNum = 0; //line number counter
		
		/*
		 * gather details we need
		 */
		
		StringBuilder sbLog = new StringBuilder("");
		String strPriority = "Medium";
		int priority = 0;
		int id = -1;
		String desc = "";
		Calendar create = new GregorianCalendar();
		Calendar lastModified = new GregorianCalendar();
		boolean complete = false;
		while(lineNum < arrFile.length)
		{
			String tag = getFirstTagValue(arrFile[lineNum]);
			if(tag.equalsIgnoreCase("ToDoItem"))
			{
				/*
				 * to decode new xml tags just add an appropriate if clause
				 * within the below while loop
				 */
				while(!getFirstTagValue(arrFile[lineNum]).equalsIgnoreCase("/ToDoItem"))
				{					
					lineNum++;
					
					tag = getFirstTagValue(arrFile[lineNum]);
					String tagVal;
					
					if(tag.equalsIgnoreCase("Priority"))
					{
						strPriority = getTagValue(arrFile[lineNum]); 
						
						//convert priority to its constant value
						priority = ToDoUtilities.convertStringToPriority(strPriority);
					}
					if(tag.equalsIgnoreCase("Description"))
					{
						desc = getTagValue(arrFile[lineNum]);
					}
					else if(tag.equalsIgnoreCase("CreateDate"))
					{
						/*
						 * should be one of either below formats:
						 * 
						 * "dd/mm/yyyy"
						 * "dd/mm/yyyy hh:mm"
						 */
						if(getTagValue(arrFile[lineNum]).length() == 16)
						{
							//"dd/mm/yyyy hh:mm"
							create = ToDoUtilities.convertStringDateTimeToCalendar(getTagValue(arrFile[lineNum]));
						}
						else if(getTagValue(arrFile[lineNum]).length() < 16)
						{
							//"dd/mm/yyyy"
							create = ToDoUtilities.convertStringDateToCalendar(getTagValue(arrFile[lineNum]));
						}
					}
					else if(tag.equalsIgnoreCase("LastModifiedDate"))
					{
						/*
						 * should be one of either below formats:
						 * 
						 * "dd/mm/yyyy"
						 * "dd/mm/yyyy hh:mm"
						 */
						
						if(getTagValue(arrFile[lineNum]).length() == 16)
						{
							//"dd/mm/yyyy hh:mm"
							lastModified = ToDoUtilities.convertStringDateTimeToCalendar(getTagValue(arrFile[lineNum]));
						}
						else if(getTagValue(arrFile[lineNum]).length() < 16)
						{
							/*
							 * this is here for backwards compatibility!
							 */
							//"dd/mm/yyyy"
							lastModified = ToDoUtilities.convertStringDateToCalendar(getTagValue(arrFile[lineNum]));
						}
					}
					else if(tag.equalsIgnoreCase("Completed"))
					{
						tagVal = getTagValue(arrFile[lineNum]);
						
						if(tagVal.equalsIgnoreCase("true")) //only need this since false is default
						{
							complete = true;
						}
						else
						{
							complete = false;
						}
					}
					else if(tag.equalsIgnoreCase("id"))
					{
						try
						{
							id = Integer.parseInt(getTagValue(arrFile[lineNum]));
						}
						catch(NumberFormatException ex)
						{
							/*
							 * id is -1 by default, an error can be detected
							 * by checking this value anyway, so no action
							 * required here.
							 */
							ex.printStackTrace();
						}
					}
					else if(tag.equalsIgnoreCase("Log"))
					{
						//since end tag is not necessarily on same line, need to increment lines until we find it
						sbLog = new StringBuilder("");
						int indexStart = indexTagOnLine("Log", arrFile[lineNum]);
						int indexEnd = indexTagOnLine("/Log", arrFile[lineNum]);
						
						if(indexEnd == -1) //end tag not on same line
						{
							//append everything on this line after tag
							sbLog.append(arrFile[lineNum].substring(indexStart+5).trim()+"\n");
							
							/*
							 * iterate lines that don't include the closing tag
							 */
							
							while (indexTagOnLine("/Log", arrFile[++lineNum]) == -1)
							{
								sbLog.append(arrFile[lineNum] + "\n");
							}
							
							//now up to line which includes ending tag
							//append text on line up to tag
							sbLog.append(arrFile[lineNum].substring(0, indexTagOnLine("/Log", arrFile[lineNum])));
							
							
						}
						else //end tag is on same line
						{
							sbLog.append(arrFile[lineNum].substring(indexStart+5, indexEnd).trim());
						}
					}	
					
				}
				/*
				 * need to filter special charaters in desc
				 */
				desc = desc.replaceAll(ToDoXMLEncoder.ENCODED_LESS_THAN, "<");
				desc = desc.replaceAll(ToDoXMLEncoder.ENCODED_GREATER_THAN, ">");
				
				ToDoItem newItem = new ToDoItem(priority, desc);
				newItem.setCreateDate(create);
				newItem.setLastModifiedDate(lastModified);
				newItem.setCompleted(complete);
				newItem.setId(id);
				
				/*
				 * need to filter special charaters in log
				 */
				String s = sbLog.toString();
				s = s.replaceAll(ToDoXMLEncoder.ENCODED_LESS_THAN, "<");
				s = s.replaceAll(ToDoXMLEncoder.ENCODED_GREATER_THAN, ">");
				newItem.setLog(new StringBuilder(s));
				
				newList.add(newItem);
				
				
			}
			lineNum++;
	
		}
		return newList;
	}
	
	public List<Alert> decodeXMLasAlertList()
	{
		List<Alert> listAlerts = new ArrayList<Alert>();
		
		String[] arrFile = xmlText.split("\n");
		
		int lineNumber = 0;
		
		while(lineNumber<arrFile.length)
		{
			AlertType alertType = AlertType.MESSAGE;
			Calendar calDateTime = Calendar.getInstance();
			int toDoID = -1;
			boolean notify = false;
			int priority = ToDoItem.PRIORITY_URGENT;
			String message = "";
			
			String tag = getFirstTagValue(arrFile[lineNumber]);
			
			if(tag.equalsIgnoreCase(ALERT_TAG)) //start reading a new alert
			{
				while(!getFirstTagValue(arrFile[lineNumber]).equalsIgnoreCase("/" + ALERT_TAG))
				{
					lineNumber++;
					
					tag = getFirstTagValue(arrFile[lineNumber]);
					
					if(tag.equalsIgnoreCase(ALERT_TYPE_TAG))
					{
						String type = getTagValue(arrFile[lineNumber]);
						
						if(type.equalsIgnoreCase(AlertType.MESSAGE.asString))
						{
							alertType = AlertType.MESSAGE;
						}
						else if(type.equalsIgnoreCase(AlertType.PRIORITY.asString))
						{
							alertType = AlertType.PRIORITY;
						}
					}
					else if(tag.equalsIgnoreCase(ALERT_DATETIME_TAG))
					{
						String strDateTime = getTagValue(arrFile[lineNumber]); 
						
						calDateTime = ToDoUtilities.convertStringDateTimeToCalendar(strDateTime);
					}
					else if(tag.equalsIgnoreCase(ALERT_TODO_ID_TAG))
					{
						String strID = getTagValue(arrFile[lineNumber]); 
						
						try
						{
							toDoID = Integer.parseInt(strID);
						}
						catch(NumberFormatException ex)
						{
							ex.printStackTrace();
						}	
					}
					else if(tag.equalsIgnoreCase(ALERT_NOTIFY_TAG))
					{
						String strNotify = getTagValue(arrFile[lineNumber]); 
						
						notify = strNotify.equalsIgnoreCase("true")? true:false;
					}
					else if(tag.equalsIgnoreCase(ALERT_PRIORITY_TAG))
					{
						String strPriority = getTagValue(arrFile[lineNumber]); 
											
						priority = ToDoUtilities.convertStringToPriority(strPriority);
					}
					else if(tag.equalsIgnoreCase(ALERT_MESSAGE_TAG))
					{
//						since end tag is not necessarily on same line, need to increment lines until we find it
						
						StringBuilder sbMessage = new StringBuilder("");
						int indexStart = indexTagOnLine(ALERT_MESSAGE_TAG, arrFile[lineNumber]);
						int indexEnd = indexTagOnLine("/"+ALERT_MESSAGE_TAG, arrFile[lineNumber]);
						
						if(indexEnd == -1) //end tag not on same line
						{
							//append everything on this line after tag
							sbMessage.append(arrFile[lineNumber].substring(indexStart+ALERT_MESSAGE_TAG.length()+2).trim()+"\n");
							
							/*
							 * iterate lines that don't include the closing tag
							 */
							
							while (indexTagOnLine("/"+ALERT_MESSAGE_TAG, arrFile[++lineNumber]) == -1)
							{
								sbMessage.append(arrFile[lineNumber] + "\n");
							}
							
							//now up to line which includes ending tag
							//append text on line up to tag
							sbMessage.append(arrFile[lineNumber].substring(0, indexTagOnLine("/"+ALERT_MESSAGE_TAG, arrFile[lineNumber])));
							
							message = sbMessage.toString();
							
						}
						else //end tag is on same line
						{
							message = getTagValue(arrFile[lineNumber]);
						}
						message = message.replaceAll(ToDoXMLEncoder.ENCODED_LESS_THAN, "<").replaceAll(ToDoXMLEncoder.ENCODED_GREATER_THAN, ">");
						
					}
				}//end while
				
				switch(alertType)
				{
				
					case MESSAGE: //generate a message alert
						
						MessageAlert mAlert = new MessageAlert();
						
						mAlert.setAlertTime(calDateTime);
						mAlert.setToDoID(toDoID);
						mAlert.setNotifier(notify);
						mAlert.setMessage(message);
						
						listAlerts.add(mAlert);
						break;
					
					case PRIORITY:
						
						PriorityAlert pAlert = new PriorityAlert();
						
						pAlert.setAlertTime(calDateTime);
						pAlert.setToDoID(toDoID);
						pAlert.setNotifier(notify);
						pAlert.setPriority(priority);
						
						listAlerts.add(pAlert);
						break;
				}//end switch
				
			}
			lineNumber++;//increment line
		}
		
		return listAlerts;
	}

	public String getXmlText() {
		return xmlText;
	}

	public void setXmlText(String xmlText) {
		this.xmlText = xmlText;
	}
	
}
