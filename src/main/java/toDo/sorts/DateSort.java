package toDo.sorts;

import java.util.*;

import toDo.data.ToDoItem;
import toDo.interfaces.ToDoSort;

public class DateSort implements ToDoSort 
{
	private List<ToDoItem> unsortedList;
	public static final int SORT_BY_CREATE_DATE = 0;
	public static final int SORT_BY_LAST_MODIFIED_DATE = 1;
	public static final int SORT_NEWEST_TO_START = 2;
	public static final int SORT_OLDEST_TO_START = 3;
	private int sortStyle, sortBy;
	
	
	public DateSort(List<ToDoItem> unsortedList, int newSortBy, int newSortType)
	{
		this(newSortBy, newSortType);
		this.unsortedList = unsortedList;
	}
	
	public DateSort(int newSortBy, int newSortType)
	{
		sortStyle = newSortType;
		sortBy = newSortBy;
	}
	
	public void setList(List<ToDoItem> unsortedList)
	{
		this.unsortedList = unsortedList;
	}
	
	public List<ToDoItem> getSortedList() 
	{
		List<ToDoItem> sortedList = new ArrayList<ToDoItem>();
		
		sortedList = quickSort(unsortedList);
		
		return sortedList;
	}
	
	/**
	 * quicksort algorithm, widely used sorting mechanism
	 */
	@SuppressWarnings("unchecked")
	public List<ToDoItem> quickSort(List<ToDoItem> aList)
	{
		List<ToDoItem> start = new ArrayList<ToDoItem>();
		List<ToDoItem> pivotList = new ArrayList<ToDoItem>();
		List<ToDoItem> end = new ArrayList<ToDoItem>();
		ToDoItem pivot;
		
		if(aList.size() <= 1)
		{
			return aList;
		}
		
		pivot = aList.get(aList.size()/2);
		
		Iterator i = aList.iterator();
		
		while(i.hasNext())
		{
			ToDoItem thisToDoItem = (ToDoItem)i.next();
			
			long pivotVal = 0;
			long thisToDoVal = 0;
			
			/*
			 * values depend which field we are sorting by
			 */
			if (sortBy == SORT_BY_CREATE_DATE)
			{
				pivotVal = pivot.getCreateDate().getTimeInMillis();
				thisToDoVal = thisToDoItem.getCreateDate().getTimeInMillis();
			}
			else if (sortBy == SORT_BY_LAST_MODIFIED_DATE)
			{
				pivotVal = pivot.getLastModifiedDate().getTimeInMillis();
				thisToDoVal = thisToDoItem.getLastModifiedDate().getTimeInMillis();
			}
						
			int compareVal = compareNums(thisToDoVal, pivotVal);
			
			if(sortStyle == SORT_OLDEST_TO_START)
			{
				//need to negate compareval
				compareVal = 0-compareVal;
			}
			
			if (compareVal > 0)
			{
				start.add(thisToDoItem);
			}
			else if (compareVal < 0)
			{
				end.add(thisToDoItem);
			}
			else //matching dates
			{
				pivotList.add(thisToDoItem);
			}
		}
		
		return concatenate(quickSort(start), pivotList, quickSort(end));
	}
	
	/**
	 * concatenate a series of lists
	 * @param lists
	 * @return
	 */
	public List<ToDoItem> concatenate(List<ToDoItem>... lists)
	{
		List<ToDoItem> newList = new ArrayList<ToDoItem>();
		
		for(List<ToDoItem> list : lists)
		{
			Iterator i = list.iterator();
			
			while(i.hasNext())
			{
				newList.add((ToDoItem)i.next());
			}
		}
			
		return newList;
	}
	
	/**
	 * returns a value greater than 0 if num1 is greater, a number less than 0 if num2 is greater
	 * or 0 if they are equal
	 * @param num1
	 * @param num2
	 * @return
	 */
	public int compareNums(long num1, long num2)
	{
		if(num1>num2)
		{
			return 1;
		}
		if(num1<num2)
		{
			return -1;
		}
		//must be equal by this point
		return 0;
	}
	
	public String toString()
	{
		String str = "Sort by ";
		
		if(sortBy == SORT_BY_CREATE_DATE)
		{
			str += "Create Date ";
		}
		else
		{
			str += "Last Modified Date ";
		}
		
		if(sortStyle == SORT_NEWEST_TO_START)
		{
			str += "descending";
		}
		else
		{
			str += "ascending";
		}
		
		return str;
	}

}
