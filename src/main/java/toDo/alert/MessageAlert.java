package toDo.alert;

import java.text.DateFormat;

import toDo.utilities.ToDoUtilities;

public class MessageAlert extends Alert
{
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String toString()
	{
		DateFormat df = DateFormat.getDateTimeInstance();
		return "MessageAlert-" + df.format(super.getAlertTime().getTime()) + "-" + super.getToDoID() + "-" + super.isNotifier() + "-" + message;
	}
	
	@Override
	public boolean equals(Object o) 
	{	
		/*
		 * compare alert type, todoID, notify, message and alert time
		 * to the accuracy of one minute
		 */
		if(o instanceof MessageAlert)
		{
			MessageAlert ma = (MessageAlert)o;
			
			if(ma.getToDoID() == getToDoID() && ma.isNotifier() == isNotifier())
			{
				if(ma.getMessage().equals(getMessage()))
				{
					String thisDateTime = ToDoUtilities.formatDateToDD_MM_YY_hh_mm(getAlertTime());
					String otherDateTime = ToDoUtilities.formatDateToDD_MM_YY_hh_mm(ma.getAlertTime());
					
					if(thisDateTime.equals(otherDateTime))
					{
						return true;
					}
				}
			}
				
		}
		return false;
	}
	
	@Override
	public int hashCode() 
	{
		/*
		 * note the +1s are added in case a value is zero (which would 
		 * cause whole hashcode to be zero).
		 */
		int id = (getToDoID()+1)*1;
		int notifier = (isNotifier()?1:2)*3;
		int message = (getMessage().length()+1)*5;
		int calendar = (int)(getAlertTime().getTimeInMillis()%10000000);
		
		return id + notifier + message + calendar;
	}
}
