package toDo.filters;

import java.util.*;

import toDo.data.ToDoItem;
import toDo.interfaces.*;
import toDo.utilities.ToDoUtilities;

public class DateFilter implements ToDoFilter
{
	private List<ToDoItem> unfilteredList;
	public static final int FILTER_TYPE_BEFORE = 0;
	public static final int FILTER_TYPE_AFTER = 1;
	public static final int FILTER_TYPE_BETWEEN = 2;
	public static final int FILTER_BY_CREATE_DATE = 3;
	public static final int FILTER_BY_LAST_MODIFIED_DATE = 4;
	private Calendar calBefore;
	private Calendar calAfter;
	private int filterType, filterBy;
	
	
	public DateFilter(List<ToDoItem> newUnfilteredList, int newFilterType, int newFilterBy)
	{
		this(newFilterType, newFilterBy);
		unfilteredList = newUnfilteredList;
	}
	
	public DateFilter(int newFilterType, int newFilterBy)
	{
		filterType = newFilterType;
		filterBy = newFilterBy;
	}
	
	public void setList(List<ToDoItem> newUnfilteredList)
	{
		unfilteredList = newUnfilteredList;
	}
	
	/**
	 * apply the dates required in order to filter the list, if the filter is
	 * of type FILTER_TYPE_BETWEEN, two dates must be provided (the first being
	 * the start date, the second being the finish date, otherwise
	 * only one must be provided
	 * @param dates
	 */
	public void applyFilterDate(Calendar... dates)
	{
		switch(filterType)
		{
			case FILTER_TYPE_AFTER:
				calAfter = dates[0];
				break;
			case FILTER_TYPE_BEFORE:
				calBefore = dates[0];
				break;
			case FILTER_TYPE_BETWEEN:
				calAfter = dates[0];
				calBefore = dates[1];
				break;
		}	
	}
	
	/**
	 * required as per ToDoFilter interface
	 */
	public List<ToDoItem> getFilteredList() 
	{
		switch(filterType)
		{
			case FILTER_TYPE_AFTER:
				return getListAfter(unfilteredList);
			case FILTER_TYPE_BEFORE:
				return getListBefore(unfilteredList);
			case FILTER_TYPE_BETWEEN:
				return getListBetween(unfilteredList);
		}
		return null;
	}
	
	/**
	 * called when filtertype is FILTER_TYPE_AFTER
	 */
	public List<ToDoItem> getListAfter(List<ToDoItem> list)
	{		
		List<ToDoItem> filteredList = new ArrayList<ToDoItem>();
		
		Iterator i = list.iterator();
		
		while(i.hasNext())
		{
			ToDoItem thisItem = (ToDoItem)i.next();
			
			/*
			 * if this date is later than startdate
			 */
			long calAfterMillis = calAfter.getTimeInMillis();;
			long thisItemMillis = 0;
			
			if(filterBy == FILTER_BY_CREATE_DATE)
			{
				thisItemMillis = thisItem.getCreateDate().getTimeInMillis();
			}
			else if(filterBy == FILTER_BY_LAST_MODIFIED_DATE)
			{
				thisItemMillis = thisItem.getLastModifiedDate().getTimeInMillis();
			}
			
			if(thisItemMillis > calAfterMillis)
			{
				filteredList.add(thisItem);
			}
		}
		return filteredList;
	}
	
	/**
	 * called when filtertype is FILTER_TYPE_BEFORE
	 */
	public List<ToDoItem> getListBefore(List<ToDoItem> list)
	{
		List<ToDoItem> filteredList = new ArrayList<ToDoItem>();
		
		Iterator i = list.iterator();
		
		while(i.hasNext())
		{
			ToDoItem thisItem = (ToDoItem)i.next();
			
			long calBeforeMillis = calBefore.getTimeInMillis();;
			long thisItemMillis = 0;
			
			if(filterBy == FILTER_BY_CREATE_DATE)
			{
				thisItemMillis = thisItem.getCreateDate().getTimeInMillis();
			}
			else if(filterBy == FILTER_BY_LAST_MODIFIED_DATE)
			{
				thisItemMillis = thisItem.getLastModifiedDate().getTimeInMillis();
			}
			
			/*
			 * if this date is before end date
			 */
			if(thisItemMillis < calBeforeMillis)
			{
				filteredList.add(thisItem);
			}
		}
		return filteredList;
	}
	
	/**
	 * called when filtertype is FILTER_TYPE_BETWEEN
	 */
	@SuppressWarnings("unchecked")
	public List<ToDoItem> getListBetween(List<ToDoItem> list)
	{
		List filteredList = new ArrayList<ToDoItem>();
		
		//apply both before and after filters to this list
		filteredList = getListBefore(list);
		filteredList = getListAfter(filteredList);
		
		return filteredList;
		
	}
	
	public String toString()
	{
		String strBy = "";
		
		switch(filterBy)
		{
			case FILTER_BY_CREATE_DATE:
				strBy = "Created";
				break;
			case FILTER_BY_LAST_MODIFIED_DATE:
				strBy = "Last Modified";
				break;
			default:
				return "Not a valid filter";
		}
		
		switch(filterType)
		{
			case FILTER_TYPE_AFTER:
				return strBy + " later than " + ToDoUtilities.formatDateToDD_MM_YY_hh_mm(calAfter);			
			case FILTER_TYPE_BEFORE:
				return strBy + " before " + ToDoUtilities.formatDateToDD_MM_YY_hh_mm(calBefore);
			case FILTER_TYPE_BETWEEN:
				return strBy + " after " + ToDoUtilities.formatDateToDD_MM_YY_hh_mm(calAfter) +
				" & before " + ToDoUtilities.formatDateToDD_MM_YY_hh_mm(calBefore);
		}
		
		return "Not a valid filter";
	}

}
