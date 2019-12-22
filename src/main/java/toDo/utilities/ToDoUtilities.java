package toDo.utilities;

import java.io.File;
import java.text.DateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import toDo.alert.Alert;
import toDo.data.*;
import toDo.gui.customComponents.AlertHolder;
import toDo.gui.customComponents.ToDoArchivedHolder;
import toDo.gui.customComponents.ToDoHolder;

public class ToDoUtilities 
{
	/**
	 * formats an object of type Calendar into a readable string
	 * @param calDate
	 * @return
	 */
	public static String formatDate(Calendar calDate)
	{
		int intDate = calDate.get(Calendar.DAY_OF_MONTH);
		String dateSuffix = getDateSuffix(intDate);
		return getCalendarMonthString(calDate.get(Calendar.MONTH))+" "+ intDate + dateSuffix  + " " + calDate.get(Calendar.YEAR);
	}
	
	/**
	 * formats an object of type Calendar into a readable string of format:
	 * dd/mm/yy hh:mm
	 * 
	 * @param calDate
	 * @return
	 */
	public static String formatDateToDD_MM_YY_hh_mm(Calendar calDate)
	{
		int day = calDate.get(Calendar.DAY_OF_MONTH);
		int month = calDate.get(Calendar.MONTH)+1;
		String year = String.valueOf(calDate.get(Calendar.YEAR)).substring(2);
		int hour = calDate.get(Calendar.HOUR_OF_DAY);
		int minute = calDate.get(Calendar.MINUTE);
		
		return appendLeadingZero(day) + "/" + appendLeadingZero(month) + "/" + year + " " + appendLeadingZero(hour) + ":" + appendLeadingZero(minute);
	}
	
	/**
	 * formats an object of type Calendar into a readable string of format:
	 * dd/mm/yyyy hh:mm
	 * 
	 * @param calDate
	 * @return
	 */
	public static String formatDateToDD_MM_YYYY_hh_mm(Calendar calDate)
	{
		int day = calDate.get(Calendar.DAY_OF_MONTH);
		int month = calDate.get(Calendar.MONTH)+1;
		String year = String.valueOf(calDate.get(Calendar.YEAR));
		int hour = calDate.get(Calendar.HOUR_OF_DAY);
		int minute = calDate.get(Calendar.MINUTE);
		
		return appendLeadingZero(day) + "/" + appendLeadingZero(month) + "/" + year + " " + appendLeadingZero(hour) + ":" + appendLeadingZero(minute);
	}
	
	/**
	 * appends leading zero to values less than 10
	 * @param num
	 * @return
	 */
	public static String appendLeadingZero(int num)
	{
		if (num<10)
		{
			return ("0" + num);
		}
		else
		{
			return num + "";
		}
	}
	
	/**
	 * converts the Calendar 'getmonth' value into a 3 letter string representing
	 * the month.
	 * @param month
	 * @return
	 */
	public static String getCalendarMonthString(int month)
	{
		String[] arrMonths = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
		return arrMonths[month];
	}

	/**
	 * returns the 2 character suffix appropriate to a given date
	 * @param date
	 * @return
	 */
	public static String getDateSuffix(int date)
	{
		switch(date)
		{
		case 1:
		case 21:
		case 31:
			return "st";
		case 2:
		case 22:
			return "nd";
		case 3:
		case 23:
			return "rd";
		}
		return "th"; //if none of the above
					
	}
	
	/**
	 * orders the provided list by the priority of the ToDoItems 
	 * secondary sort if by alphabetical order
	 * @param toDoList
	 */
	public static List<ToDoHolder> orderByPriority(List<ToDoHolder> toDoList)
	{
		//1. sort alphabetically
		//2. sort by priority
		
		List<ToDoHolder> sortedList = new ArrayList<ToDoHolder>();
		
		List<ToDoHolder> alphabeticalList = orderAlpabetically(toDoList);
		
		Iterator i;
		
		/*
		 * first put urgent items into new list
		 */
		
		i = alphabeticalList.iterator();
		
		while (i.hasNext())
		{
			ToDoHolder holder = (ToDoHolder)i.next();
			if(holder.getToDoItem().getPriority() == ToDoItem.PRIORITY_URGENT)
			{
				sortedList.add(holder);
			}
		}
		
		/*
		 * next put high priority items into new list
		 */
		
		i = alphabeticalList.iterator();
		
		while (i.hasNext())
		{
			ToDoHolder holder = (ToDoHolder)i.next();
			if(holder.getToDoItem().getPriority() == ToDoItem.PRIORITY_HIGH)
			{
				sortedList.add(holder);
			}
		}
		
		/*
		 * next put medium priority items into new list
		 */
		
		i = alphabeticalList.iterator();
		
		while (i.hasNext())
		{
			ToDoHolder holder = (ToDoHolder)i.next();
			if(holder.getToDoItem().getPriority() == ToDoItem.PRIORITY_MEDIUM)
			{
				sortedList.add(holder);
			}
		}
		
		/*
		 * next put low priority items into new list
		 */
		
		i = alphabeticalList.iterator();
		
		while (i.hasNext())
		{
			ToDoHolder holder = (ToDoHolder)i.next();
			if(holder.getToDoItem().getPriority() == ToDoItem.PRIORITY_LOW)
			{
				sortedList.add(holder);
			}
		}
		
		return sortedList;
	}
	
	/**
	 * converts a string of format dd/mm/yyyy to a calendar object
	 * 
	 */
	public static Calendar convertStringDateToCalendar(String strCal)
	{
		//"dd/mm/yyyy"
		Calendar c = new GregorianCalendar();
		
		String[] arrCal = strCal.split("/");
		
		int day = Integer.parseInt(arrCal[0]);
		int month = Integer.parseInt(arrCal[1]);
		int year = Integer.parseInt(arrCal[2]);
		
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.MONTH, month-1);
		c.set(Calendar.YEAR, year);
		
		return c;
	}
	
	
	/**
	 * converts a string of format dd/mm/yyyy hh:mm to a calendar object
	 * 
	 */
	public static Calendar convertStringDateTimeToCalendar(String strCal)
	{
		//"dd/mm/yyyy hh:mm"
		Calendar c = new GregorianCalendar();
		
		String[] arrSlash = strCal.split("/");
		
		//arrCal is now {DD,MM,YYYY HH:MM}
		
		int day = Integer.parseInt(arrSlash[0]);
		int month = Integer.parseInt(arrSlash[1]);
		
		String[] arrSpace = arrSlash[2].split(" ");
		
		//arrSpace is now {YYYY,HH:MM}
		
		int year = Integer.parseInt(arrSpace[0]);
		
		String[] arrColon = arrSpace[1].split(":");
		
		//arrColon is now {HH,MM}
		
		int hour = Integer.parseInt(arrColon[0]);
		int minute = Integer.parseInt(arrColon[1]);
		
		//set values to object
		c.set(Calendar.DAY_OF_MONTH, day);
		c.set(Calendar.MONTH, month-1);
		c.set(Calendar.YEAR, year);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		
		return c;
	}
	
	public static List<ToDoHolder> orderAlpabetically(List<ToDoHolder> list)
	{
		List<ToDoHolder> sortedList = new ArrayList<ToDoHolder>();
		List<ToDoHolder> tempList = new ArrayList<ToDoHolder>();
		
		/*
		 * copy existing list
		 */
		
		Iterator i = list.iterator();
		
		while(i.hasNext())
		{
			tempList.add((ToDoHolder)i.next());
		}
		
		/*
		 * add holders in sorted list by getting them from tempList one at a time
		 * in the correct order and deleting when transferred
		 */
		
		while (tempList.size() != 0)
		{
			boolean firstPass = true;
			int index = 0;
			int lexicographicallyFirstIndex = 0;
			String lexicographicallyFirstDesc = null;
			
			i = tempList.iterator();
			
			/*
			 * while loop identifies which is the lexicographically first holder
			 * based on description
			 */
			while(i.hasNext())
			{
				ToDoHolder thisHolder = (ToDoHolder)i.next();
				if (firstPass)
				{
					firstPass = false;
					lexicographicallyFirstDesc = thisHolder.getDescription();
					//index already 0
				}
				else
				{
					String thisDesc = thisHolder.getDescription();
					
					//see if current holder is alphabetically first
					if (lexicographicallyFirstDesc.compareToIgnoreCase(thisDesc) > 0)
					{
						lexicographicallyFirstDesc = thisDesc;
						lexicographicallyFirstIndex = index;
					}
				}
				index++;
			}//end while
			
			//add to sorted list & remove from templist
			sortedList.add(tempList.get(lexicographicallyFirstIndex));
			tempList.remove(lexicographicallyFirstIndex);
			
		}//end while
		
		return sortedList;
	}
	
	/**
	 * converts a list of ToDoHolder objects to a list of ToDoItem objects
	 * @param holders
	 * @return
	 */
	public static List<ToDoItem> convertHolderListToItemList(List<ToDoHolder> holders)
	{
		List<ToDoItem> newList = new ArrayList<ToDoItem>();
		
		Iterator i = holders.iterator();
		
		while(i.hasNext())
		{
			ToDoHolder thisHolder = (ToDoHolder)i.next();
			newList.add(thisHolder.getToDoItem());
		}
		
		return newList;
	}
	
	/**
	 * converts a list of ToDoArchivedHolder objects to a list of ToDoItem objects
	 * @param holders
	 * @return
	 */
	public static List<ToDoItem> convertArchivedHolderListToItemList(List<ToDoArchivedHolder> holders)
	{
		List<ToDoItem> newList = new ArrayList<ToDoItem>();
		
		Iterator i = holders.iterator();
		
		while(i.hasNext())
		{
			ToDoArchivedHolder thisHolder = (ToDoArchivedHolder)i.next();
			newList.add(thisHolder.getToDoItem());
		}
		
		return newList;
	}
	
	public static boolean isValidDateTime(int DD, int MM, int YYYY, int hh, int mm)
	{
		boolean isLeapYear = false;
		
		if(YYYY < 2000 || YYYY > 2100)
		{
			return false;
		}
		
		if(YYYY % 4 == 0)
		{
			isLeapYear = true;
		}
		
		if(MM < 1 || MM > 12)
		{
			return false;
		}
		
		if(DD < 1 || DD > getDaysInMonth(MM, isLeapYear))
		{
			return false;
		}
		
		if(hh > 23 || hh<0)
		{
			return false;
		}
		
		if(mm < 0 || mm > 59)
		{
			return false;
		}
		
		//if we get to this point it is valid
		return true;
	}
	
	/**
	 * January is 1, decemeber is 12
	 */
	public static int getDaysInMonth(int month, boolean isLeapYear)
	{
		switch(month)
		{
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				return 31;
			case 2:
				if(isLeapYear)
				{
					return 29;
				}
				else
				{
					return 28;
				}
			case 4:
			case 6:
			case 9:
			case 11:
				return 30;
		}
		//shouldnt reach this point
		return -1;
	}
	
	public static List<ToDoArchivedHolder> convertItemListToArchivedHolderList(List<ToDoItem> itemList)
	{
		List<ToDoArchivedHolder> newList = new ArrayList<ToDoArchivedHolder>();
		
		Iterator i = itemList.iterator();
		
		while (i.hasNext())
		{
			ToDoItem thisItem = (ToDoItem)i.next();
			
			newList.add(new ToDoArchivedHolder(thisItem));
		}
		return newList;
	}
	
	public static List<ToDoHolder> convertItemListToHolderList(List<ToDoItem> itemList)
	{
		List<ToDoHolder> newList = new ArrayList<ToDoHolder>();
		
		Iterator i = itemList.iterator();
		
		while (i.hasNext())
		{
			ToDoItem thisItem = (ToDoItem)i.next();
			
			newList.add(new ToDoHolder(thisItem));
		}
		return newList;
	}
	
	/**
	 * matches the holder by priority, description, createDate, lastModifiedDate
	 * and completed status - does not compare log as really the dates
	 * should almost certainly be unique anyway and logs would be memory
	 * intensive as they can be long
	 * @param holder
	 * @param list
	 * @return
	 */
	public static int getIndexOfArchivedHolderInList(ToDoArchivedHolder holder, List<ToDoArchivedHolder> list)
	{
		int count = 0;
		Iterator i = list.iterator();
		
		while(i.hasNext())
		{
			ToDoArchivedHolder thisHolder = (ToDoArchivedHolder)i.next();
			
			if (toDoItemsAreIdentical(holder.getToDoItem(), thisHolder.getToDoItem()))
			{
				return count;
			}
			
			count++;
		}
		return -1;
	}
	
	public static boolean toDoItemsAreIdentical(ToDoItem item1, ToDoItem item2)
	{
		if (item1.getPriority() != item2.getPriority())
		{
			return false;
		}
		
		if (!item1.getDescription().equals(item2.getDescription()))
		{
			return false;
		}
		
		if (!areEqualDates(item1.getCreateDate(), item2.getCreateDate()))
		{
			return false;
		}
		
		if (!areEqualDates(item1.getLastModifiedDate(), item2.getLastModifiedDate()))
		{
			return false;
		}
		
		if (item1.isCompleted() != item2.isCompleted())
		{
			return false;
		}
		return true;
	}
	
	/**
	 * compares dates to the minute, differences less than a minute will not
	 * be detected
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static boolean areEqualDates(Calendar c1, Calendar c2)
	{
		if (c1.get(Calendar.MINUTE) != c1.get(Calendar.MINUTE))
		{
			return false;
		}
		
		if (c1.get(Calendar.HOUR) != c1.get(Calendar.HOUR))
		{
			return false;
		}
		
		if (c1.get(Calendar.DAY_OF_YEAR) != c1.get(Calendar.DAY_OF_YEAR))
		{
			return false;
		}
		
		if (c1.get(Calendar.YEAR) != c1.get(Calendar.YEAR))
		{
			return false;
		}
		return true;
	}
	
	/**
	 * returns a new list which is the reversed version of the one supplied
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static List reverse(List list)
	{
		List newList = new ArrayList();
		
		for(int x = list.size()-1; x>=0; x--)
		{
			newList.add(list.get(x));
		}
		
		return newList;
		
		
	}
	
	/**
	 * matches the holder by priority, description, createDate, lastModifiedDate
	 * and completed status - does not compare log as really the dates
	 * should almost certainly be unique anyway and logs would be memory
	 * intensive as they can be long
	 * @param holder
	 * @param list
	 * @return
	 */
	public static int getIndexOfHolderInList(ToDoHolder holder, List<ToDoHolder> list)
	{
		int count = 0;
		Iterator i = list.iterator();
		
		while(i.hasNext())
		{
			ToDoHolder thisHolder = (ToDoHolder)i.next();
			
			if (toDoItemsAreIdentical(holder.getToDoItem(), thisHolder.getToDoItem()))
			{
				return count;
			}
			
			count++;
		}
		return -1;
	}
	
	/**
	 * method to get earliest create date of any todo from a give list of todoitems.
	 * setting negate to true will make this method get the latest date
	 * @param listItems
	 * @param negate
	 * @return
	 */
	public static Calendar getEarliestLastModifiedDate(List<ToDoItem> listItems, boolean negate)
	{
		boolean firstPass = true;
		Calendar earliestCal = Calendar.getInstance();
		
		Iterator i = listItems.iterator();
		
		while(i.hasNext())
		{
			ToDoItem thisItem = (ToDoItem)i.next();
			
			if(firstPass)
			{
				earliestCal = thisItem.getLastModifiedDate();
				firstPass = false;
			}
			else
			{
				if(negate)//effectively makes this method get latest date
				{
					if(thisItem.getLastModifiedDate().getTimeInMillis() > earliestCal.getTimeInMillis())
					{
						earliestCal = thisItem.getLastModifiedDate();
					}
				}
				else
				{
					if(thisItem.getLastModifiedDate().getTimeInMillis() < earliestCal.getTimeInMillis())
					{
						earliestCal = thisItem.getLastModifiedDate();
					}
				}
			}
		}
		
		return earliestCal;
	}
	
	/**
	 * method for ordering archives by number (alphabetically 10 comes before 2
	 * so need to custom sort this. This method assumes filename as
	 * past_archive_<num>.data
	 * @param files
	 * @return
	 */
	public static void sortFiles(List<File> files)
	{
		/*
		 * bubblesort algorithm
		 */
		boolean changesMade = true;
		
		while (changesMade)
		{
			changesMade = false;
			
			for(int x=1; x<files.size(); x++)
			{
				/*
				 * if file at index x has a lower number than the file
				 * at index x-1
				 */
				if(getFileNum(files.get(x)) < getFileNum(files.get(x-1)))
				{
					/*
					 * put this file in the previous index
					 */
					File tempFile = files.get(x);
					files.remove(x);
					files.add(x-1, tempFile);
					
					//indicate changes were made
					changesMade = true;
				}
			}
			
		}
		
	}
	
	/**
	 * get the archive number ie past_archive_<this>.data
	 * @param f
	 * @return
	 */
	public static int getFileNum(File f)
	{
		String fileStr = f.toString().substring(2);
		
		int num = Integer.parseInt(fileStr.substring(13, fileStr.length()-5));
		return num;
	}
	
	/**
	 * allocates unique ids to any todos in the list that have not been allocated
	 * an id (default id is -1, so if a todo has an id of -1 it is changed).
	 * 
	 * returns true if any changes were made.
	 * @param listToDos
	 */
	public static boolean allocateIds(List<ToDoItem> listToDos)
	{
		boolean changesMade = false;
		List<Integer> listUsedIds = getUsedIds(listToDos);
		
		for(ToDoItem item: listToDos)
		{
			if (item.getId() <= 0)//id needs to be allocated
			{
				changesMade = true;//flag that changes have been made
				
				for(int i=1; i<=1000000; i++)//one million different possible ids
				{
					
					if(!listUsedIds.contains(new Integer(i)))//if id not in use
					{
						/*
						 * appropriate id found, allocate to this todo and quit
						 * loop
						 */
						item.setId(i);
						listUsedIds.add(i);//add this id to list of used ones
						break;
					}
				}
			}
		}
		
		return changesMade;
	}
	
	/**
	 * returns a list of wrapped ints indicating which ids are currently in 
	 * use and therefore not available
	 * @param listToDos
	 * @return
	 */
	public static List<Integer> getUsedIds(List<ToDoItem> listToDos)
	{
		List<Integer> listIds = new ArrayList<Integer>();
		
		for(ToDoItem item: listToDos)
		{
			if(item.getId() != -1)
			{
				listIds.add(item.getId());
			}
		}
		
		return listIds;
	}
	
	public static int convertStringToPriority(String str)
	{
		if (str.equalsIgnoreCase("urgent"))
		{
			return ToDoItem.PRIORITY_URGENT;
		}
		else if (str.equalsIgnoreCase("high"))
		{
			return ToDoItem.PRIORITY_HIGH;
		}
		else if (str.equalsIgnoreCase("medium"))
		{
			return ToDoItem.PRIORITY_MEDIUM;
		}
		else if (str.equalsIgnoreCase("low"))
		{
			return ToDoItem.PRIORITY_LOW;
		}
		else
		{
			return -1;
		}
	}
	
	/**
	 * returns the first todo with a matching id in the list provided. 
	 * returns null if no match found.
	 * @param id
	 * @param listToDos
	 * @return
	 */
	public static ToDoItem getToDoByID(int id, List<ToDoItem> listToDos)
	{
		for(ToDoItem item: listToDos)
		{
			if (item.getId() == id)
			{
				return item;
			}
		}
		return null;
	}
	
	/**
	 * wraps the contents of the provided list of alerts into alert holders,
	 * then puts them into a list of the correct type and passes them back
	 * @param listAlert
	 * @param listToDos
	 * @return
	 */
	public static List<AlertHolder> wrapAlerts(List<Alert> listAlert, List<ToDoItem> listToDos)
	{
		List<AlertHolder> listHolder = new ArrayList<AlertHolder>();
		
		for(Alert a: listAlert)
		{
			listHolder.add(new AlertHolder(a, listToDos));
		}
		
		return listHolder;
	}
	
	public static List<File> filterAutoSaves(File[] files)
	{
		List<File> fileList = new ArrayList<File>();
		
		for(File f : files)
		{
			String fullFileStr = f.toString();
			//get ending filename only eg "todo.data"
			String fileStr = fullFileStr.substring(fullFileStr.lastIndexOf("\\")+1);
			String regex = "(\\d\\d\\d\\d)_" +
					"(\\d\\d)_" +
					"(\\d\\d)_" +
					"(\\d\\d)_" +
					"(\\d\\d)" +
					"(\\.)" +
					"data";
			if(matchText(fileStr, regex))
			{
				fileList.add(f);
			}
		}
		
		return fileList;
	}
	
	public static boolean matchText(String text, String regex)
	{
		Pattern p = Pattern.compile(regex);
		
		Matcher m = p.matcher(text);
		
		return m.matches();
	}

	public static LocalDate convertCalendarToLocalDate(Calendar cal) {
		return LocalDate.of(
				cal.get(Calendar.YEAR),
				cal.get(Calendar.MONTH) + 1,
				cal.get(Calendar.DAY_OF_MONTH));
	}

	public static String formatCalendar(Calendar cal) {
		if(cal == null) {
			return null;
		}
		return DateFormat.getDateTimeInstance().format(cal.getTime());
	}
}
