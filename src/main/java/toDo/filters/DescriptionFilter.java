package toDo.filters;

import java.util.*;

import toDo.data.ToDoItem;
import toDo.interfaces.ToDoFilter;
import java.util.regex.*;

public class DescriptionFilter implements ToDoFilter
{
	private String[] searchTerms;
	private List<ToDoItem> unfilteredList;
	//defaults:
	private boolean caseSensitive = false;
	private boolean needAllSearchTerms = true;
	
	public DescriptionFilter()
	{
		
	}
	
	public DescriptionFilter(List<ToDoItem> unfilteredList)
	{
		this.unfilteredList = unfilteredList;
	}
	
	/**
	 * get a list with filters applied
	 */
	public List<ToDoItem> getFilteredList() 
	{
		List<ToDoItem> filteredList = new ArrayList<ToDoItem>();
		
		for(ToDoItem item: unfilteredList)
		{
			//flag to indicate if this item matches required criteria
			boolean okFlag;
			
			if(needAllSearchTerms)
			{
				okFlag = true;
				for(String keyword: searchTerms)
				{
					if(!containsKeyword(item.getDescription(), keyword))
					{
						/*
						 * doesn't match this keyword, therefore doesn't contain
						 * all search terms, mark flag as false and quit loop
						 */
						okFlag = false;
						break;
					}
						
				}
			}
			else//only needs to match one search term
			{
				okFlag = false;
				for(String keyword: searchTerms)
				{
					if(containsKeyword(item.getDescription(), keyword))
					{
						/*
						 * a match has been found, only one is needed so 
						 * mark flag as true and quit loop
						 */
						okFlag = true;
						break;
					}				
				}
			}
			
			if(okFlag)//this item matched criteria
			{
				filteredList.add(item);
			}
			
		}
		
		return filteredList;
	}

	/**
	 * set the list to be filtered
	 */
	public void setList(List<ToDoItem> newList) 
	{
		unfilteredList = newList;
	}
	
	public void setSearchString(String keywords, String delimiter)
	{
		searchTerms = keywords.split(delimiter);
	}
	
	/**
	 * uses pattern matching to check if provided string contains
	 * provided keyword
	 * @param str
	 * @param keyword
	 * @return
	 */
	public boolean containsKeyword(String str, String keyword)
	{
		Pattern p;
		Matcher m;
		if(caseSensitive)
		{
			p = Pattern.compile("(.*)" + keyword + "(.*)");
			m = p.matcher(str);
		}
		else//not case sensitive
		{
			p = Pattern.compile("(.*)" + keyword.toLowerCase() + "(.*)");
			m = p.matcher(str.toLowerCase());
		}
	
		return m.matches();
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive(boolean caseSensitive) {
		this.caseSensitive = caseSensitive;
	}

	public boolean isNeedAllSearchTerms() {
		return needAllSearchTerms;
	}

	public void setNeedAllSearchTerms(boolean needAllSearchTerms) {
		this.needAllSearchTerms = needAllSearchTerms;
	}
	
	/**
	 * note this method tries to use as few concatenations as possible
	 */
	public String toString()
	{
		String filterAsString = "";
		
		if(!caseSensitive)
		{
			filterAsString+= "Non-CS ";
		}
		else
		{
			filterAsString+= "CS ";
		}
		
		if(needAllSearchTerms)
		{
			filterAsString+= "Search for all of '";
		}
		else
		{
			filterAsString+= "Search for any of '";
		}
		
		for(String str: searchTerms)
		{
			filterAsString+= str + ",";
		}
		
		//remove last comma
		filterAsString = filterAsString.substring(0, filterAsString.length()-1) + "'";
		
		//limited display space:
		if(filterAsString.length()>65)
		{
			filterAsString = filterAsString.substring(0, 63) + "...";
		}
		
		return filterAsString;
	}

}
