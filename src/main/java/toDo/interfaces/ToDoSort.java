package toDo.interfaces;

import java.util.*;

import toDo.data.*;

public interface ToDoSort 
{
	public List<ToDoItem> getSortedList();
	public void setList(List<ToDoItem> newList);
}
