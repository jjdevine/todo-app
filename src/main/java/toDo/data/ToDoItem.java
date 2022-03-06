package toDo.data;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class ToDoItem 
{
	/*
	 * ID added for v1.2 - alerting
	 */
	private int id;
	private int priority;
	private String description;
	private Calendar createDate;
	private Calendar lastModifiedDate;
	private boolean completed;
	private StringBuilder log;
	private ToDoSchedule schedule;
	public static final int PRIORITY_URGENT = 0;
	public static final int PRIORITY_HIGH = 1;
	public static final int PRIORITY_MEDIUM = 2;
	public static final int PRIORITY_LOW = 3;
	
	public static final int LOG_AUDIT = 0;
	public static final int LOG_COMMENT = 1;
	public static final int LOG_ALERT = 2;

	public ToDoItem(int newPriority, String newDescription)
	{
		priority = newPriority;
		description = newDescription;
		createDate = new GregorianCalendar();
		lastModifiedDate = new GregorianCalendar();
		completed = false;
		log = new StringBuilder("");
		id = -1;
	}
	
	public void addToLog(int submissionType, String text)
	{
		StringBuilder sb = new StringBuilder("");
		
		if(submissionType == LOG_AUDIT)
		{
			sb.append("AUDIT - ");
			
		}
		else if (submissionType == LOG_COMMENT)
		{
			sb.append("COMMENT - ");
		}
		else if (submissionType == LOG_ALERT)
		{
			sb.append("ALERT - ");
		}
		
		sb.append(new GregorianCalendar().getTime());
		sb.append("\n\n");
		sb.append(text);
		sb.append("\n\n");
		
		/*
		 * since whenever any modification is made, a change to the log is made,
		 * it is safe to update last modified date here
		 */

		lastModifiedDate = new GregorianCalendar();
		
		log.append((CharSequence)sb);
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Calendar getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Calendar lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public Calendar getCreateDate() {
		return createDate;
	}

	public StringBuilder getLog() {
		return log;
	}

	public void setLog(StringBuilder log) {
		this.log = log;
	}

	public void setCreateDate(Calendar createDate) {
		this.createDate = createDate;
	}

	public static String priorityAsString(int priority)
	{
		switch(priority)
		{
			case ToDoItem.PRIORITY_URGENT:
				return "Urgent";
			case ToDoItem.PRIORITY_HIGH:
				return "High";
			case ToDoItem.PRIORITY_MEDIUM:
				return "Medium";
			case ToDoItem.PRIORITY_LOW:
				return "Low";
			default: //shouldn't happen though
				return "n/a";				
		}
	}

	public String priorityAsString() {
		return priorityAsString(priority);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ToDoSchedule getSchedule() {
		return schedule;
	}

	public void setSchedule(ToDoSchedule schedule) {
		this.schedule = schedule;
	}
}
