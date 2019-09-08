package toDo.filters;

import java.util.*;
import toDo.data.*;
import toDo.interfaces.*;

public class PriorityFilter implements ToDoFilter
{
	public static final int PRIORITY_LOW = 0;
	public static final int PRIORITY_MEDIUM = 1;
	public static final int PRIORITY_HIGH = 2;
	public static final int PRIORITY_URGENT = 3;
	private boolean PRIORITY_LOW_FILTERED_OUT = false;
	private boolean PRIORITY_MEDIUM_FILTERED_OUT = false;
	private boolean PRIORITY_HIGH_FILTERED_OUT = false;
	private boolean PRIORITY_URGENT_FILTERED_OUT = false;
	
	private List unfilteredList;
	
	public PriorityFilter(List<ToDoItem> newUnfilteredList)
	{
		unfilteredList = newUnfilteredList;
	}
	
	public PriorityFilter()
	{
		
	}
	
	public void setList(List<ToDoItem> newUnfilteredList)
	{
		unfilteredList = newUnfilteredList;
	}
	
	/**
	 * specify which priorities are to be filtered out
	 */
	public void applyFilter(int... filters)
	{
		/*
		 * user provides 0 to many filters which are then applied, if an item 
		 * is filtered it does not turn up in the final list
		 */

		for(int priority: filters) //iterate supplied filters
		{
			switch(priority)
			{
				case PRIORITY_LOW:
					PRIORITY_LOW_FILTERED_OUT = true;
					break;
					
				case PRIORITY_MEDIUM:
					PRIORITY_MEDIUM_FILTERED_OUT = true;
					break;
					
				case PRIORITY_HIGH:
					PRIORITY_HIGH_FILTERED_OUT = true;
					break;
					
				case PRIORITY_URGENT:
					PRIORITY_URGENT_FILTERED_OUT = true;
					break;
			}
		}
		
	}
	
	
	/**
	 * specify which priorities are to be allowed into the list (by default
	 * all priorities are allowed in the list
	 */
	public void removeFilter(int... filters)
	{
		/*
		 * user provides 0 to many filters which are then removed, if an item 
		 * is not filtered it will be in the final list
		 */

		for(int priority: filters) //iterate supplied filters
		{
			switch(priority)
			{
				case PRIORITY_LOW:
					PRIORITY_LOW_FILTERED_OUT = false;
					break;
					
				case PRIORITY_MEDIUM:
					PRIORITY_MEDIUM_FILTERED_OUT = false;
					break;
					
				case PRIORITY_HIGH:
					PRIORITY_HIGH_FILTERED_OUT = false;
					break;
					
				case PRIORITY_URGENT:
					PRIORITY_URGENT_FILTERED_OUT = false;
					break;
			}
		}
		
	}
	
	/**
	 * returns new list with filters applied
	 */
	@SuppressWarnings("unchecked")
	public List<ToDoItem> getFilteredList()
	{
		List filteredList = new ArrayList<ToDoItem>();
		
		Iterator i = unfilteredList.iterator();
		
		while (i.hasNext())
		{
			ToDoItem thisItem = (ToDoItem)i.next();
			
			int thisPriority = thisItem.getPriority();
			
			switch(thisPriority)
			{
				case ToDoItem.PRIORITY_LOW:
					if(!PRIORITY_LOW_FILTERED_OUT)
					{
						filteredList.add(thisItem);
					}
					break;
					
				case ToDoItem.PRIORITY_MEDIUM:
					if(!PRIORITY_MEDIUM_FILTERED_OUT)
					{
						filteredList.add(thisItem);
					}
					break;
					
				case ToDoItem.PRIORITY_HIGH:
					if(!PRIORITY_HIGH_FILTERED_OUT)
					{
						filteredList.add(thisItem);
					}
					break;
					
				case ToDoItem.PRIORITY_URGENT:
					if(!PRIORITY_URGENT_FILTERED_OUT)
					{
						filteredList.add(thisItem);
					}
					break;
			}
		}
		
		return filteredList;
	}
	
	public String toString()
	{
		String str = "Allow ";
		
		//marker to indicate if this is first parameter to display
		boolean first = true; 
		
		if(!PRIORITY_URGENT_FILTERED_OUT)
		{
			str += "Urgent";
			first = false;
		}
		
		if(!PRIORITY_HIGH_FILTERED_OUT)
		{
			if (first)
			{
				str += "High";
				first = false;
			}
			else
			{
				str += ", High";
			}
		}
		
		if(!PRIORITY_MEDIUM_FILTERED_OUT)
		{
			if (first)
			{
				str += "Medium";
				first = false;
			}
			else
			{
				str += ", Medium";
			}
		}
		
		if(!PRIORITY_LOW_FILTERED_OUT)
		{
			if (first)
			{
				str += "Low";
				first = false;
			}
			else
			{
				str += ", Low";
			}
		}
		
		return str + " priorities only";
	}
}
