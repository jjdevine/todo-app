package toDo.sorts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import toDo.interfaces.*;
import toDo.data.ToDoItem;

public class DescriptionSort implements ToDoSort
{
	public static final int SORT_ASCENDING = 0;
	public static final int SORT_DESCENDING = 1;
	private int sortStyle;
	private List<ToDoItem> unsortedList;
	
	public DescriptionSort(List<ToDoItem> itemList, int newSortStyle)
	{
		this(newSortStyle);
		unsortedList = itemList;
	}
	
	public DescriptionSort(int newSortStyle)
	{
		sortStyle = newSortStyle;
	}
	
	public void setList(List<ToDoItem> unsortedList)
	{
		this.unsortedList = unsortedList;
	}

	public List<ToDoItem> getSortedList() 
	{		
		return quickSort(unsortedList);
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
			
			int compareVal = thisToDoItem.getDescription().compareToIgnoreCase(pivot.getDescription());
			
			if(sortStyle == SORT_ASCENDING)
			{
				//need to negate compareval
				compareVal = 0-compareVal;
			}
			
			if (compareVal < 0)
			{
				start.add(thisToDoItem);
			}
			else if (compareVal > 0)
			{
				end.add(thisToDoItem);
			}
			else //matching string
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
	
	public String toString()
	{
		String str = "Sort by Description ";
		
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
