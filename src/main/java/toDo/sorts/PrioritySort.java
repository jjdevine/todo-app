package toDo.sorts;

import java.util.*;
import toDo.data.*;
import toDo.interfaces.*;

public class PrioritySort implements ToDoSort
{
	public static final int SORT_ASCENDING = 0;
	public static final int SORT_DESCENDING = 1;
	private int sortStyle;
	private List<ToDoItem> unsortedList;
	
	public PrioritySort(List<ToDoItem> itemList, int newSortStyle)
	{
		this(newSortStyle);
		unsortedList = itemList;
	}
	
	public PrioritySort(int newSortStyle)
	{
		sortStyle = newSortStyle;
	}
	
	public void setList(List<ToDoItem> unsortedList)
	{
		this.unsortedList = unsortedList;
	}
	
	public List<ToDoItem> getSortedList() 
	{
		List<ToDoItem> sortedList = new ArrayList<ToDoItem>();
		
		if(sortStyle == SORT_ASCENDING)
		{
			addLowPriorities(sortedList);
			addMediumPriorities(sortedList);
			addHighPriorities(sortedList);
			addUrgentPriorities(sortedList);
		}
		else if(sortStyle == SORT_DESCENDING)
		{
			addUrgentPriorities(sortedList);
			addHighPriorities(sortedList);
			addMediumPriorities(sortedList);
			addLowPriorities(sortedList);		
		}
		
		return sortedList;
	}
	
	public void addLowPriorities(List<ToDoItem> list)
	{
		Iterator i = unsortedList.iterator();
		
		while(i.hasNext())
		{
			ToDoItem thisItem = (ToDoItem)i.next();
			
			if(thisItem.getPriority() == ToDoItem.PRIORITY_LOW)
			{
				list.add(thisItem);
			}
		}
		
	}
	
	public void addMediumPriorities(List<ToDoItem> list)
	{
		Iterator i = unsortedList.iterator();
		
		while(i.hasNext())
		{
			ToDoItem thisItem = (ToDoItem)i.next();
			
			if(thisItem.getPriority() == ToDoItem.PRIORITY_MEDIUM)
			{
				list.add(thisItem);
			}
		}
		
	}
	
	public void addHighPriorities(List<ToDoItem> list)
	{
		Iterator i = unsortedList.iterator();
		
		while(i.hasNext())
		{
			ToDoItem thisItem = (ToDoItem)i.next();
			
			if(thisItem.getPriority() == ToDoItem.PRIORITY_HIGH)
			{
				list.add(thisItem);
			}
		}
		
	}
	
	public void addUrgentPriorities(List<ToDoItem> list)
	{
		Iterator i = unsortedList.iterator();
		
		while(i.hasNext())
		{
			ToDoItem thisItem = (ToDoItem)i.next();
			
			if(thisItem.getPriority() == ToDoItem.PRIORITY_URGENT)
			{
				list.add(thisItem);
			}
		}
		
	}
	
	public String toString()
	{
		String str = "Sort by Priority ";
		
		if(sortStyle == SORT_ASCENDING)
		{
			str += "ascending";
		}
		else if(sortStyle == SORT_DESCENDING)
		{
			str += "descending";
		}
		
		return str;
	}
	


}
