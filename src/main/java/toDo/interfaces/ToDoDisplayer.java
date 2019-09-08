package toDo.interfaces;

import java.util.*;

import toDo.data.ToDoItem;

public interface ToDoDisplayer 
{
	public void refreshToDoDisplay();
	public List getToDoList();
	public void setFilters(List<ToDoFilter> filters);
	public void setSorts(List<ToDoSort> sorts);
	public List<ToDoFilter> getFilters();
	public List<ToDoSort> getSorts();
	public void deleteToDoItem(ToDoItem item);
	public List<? extends Object> getFilteredSortedToDoList();
	public List<ToDoItem> getToDoItemList();
}
