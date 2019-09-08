package toDo.alert;

import java.util.*;

public abstract class Alert implements Comparable<Alert>
{
	//initialize with default values
	private Calendar alertTime = Calendar.getInstance();
	private int toDoID = -1;
	private boolean notifier = false;
	
	public Calendar getAlertTime() {
		return alertTime;
	}
	public void setAlertTime(Calendar alertTime) {
		this.alertTime = alertTime;
	}
	public boolean isNotifier() {
		return notifier;
	}
	public void setNotifier(boolean notify) {
		this.notifier = notify;
	}
	public int getToDoID() {
		return toDoID;
	}
	public void setToDoID(int toDoID) {
		this.toDoID = toDoID;
	}

	public int compareTo(Alert a)
	{
		return getAlertTime().compareTo(a.getAlertTime());
	}

}
