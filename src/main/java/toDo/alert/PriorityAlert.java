package toDo.alert;

import java.text.DateFormat;

import toDo.data.ToDoItem;
import toDo.utilities.ToDoUtilities;

public class PriorityAlert extends Alert
{
	private int priority;

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String toString()
	{
		DateFormat df = DateFormat.getDateTimeInstance();
		return "PriorityAlert-" + df.format(super.getAlertTime().getTime()) + "-" + super.getToDoID() + "-" + super.isNotifier() + "-" + ToDoItem.priorityAsString(priority);
	}

	@Override
	public boolean equals(Object o) 
	{	
		/*
		 * compare alert type, todoID, notify, priority and alert time
		 * to the accuracy of one minute
		 */
		if(o instanceof PriorityAlert)
		{
			PriorityAlert pa = (PriorityAlert)o;
			
			if(pa.getToDoID() == getToDoID() && pa.isNotifier() == isNotifier())
			{
				if(pa.getPriority()== getPriority())
				{
					String thisDateTime = ToDoUtilities.formatDateToDD_MM_YY_hh_mm(getAlertTime());
					String otherDateTime = ToDoUtilities.formatDateToDD_MM_YY_hh_mm(pa.getAlertTime());
					
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
		int priority = (getPriority() == 0? getPriority()+1: getPriority())*5;
		int calendar = (int)(getAlertTime().getTimeInMillis()%10000000);
		
		return id + notifier + priority + calendar;
	}
}
