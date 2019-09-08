package toDo.interfaces;

import java.util.*;
import toDo.data.*;

public interface ToDoFilter 
{
	public List<ToDoItem> getFilteredList();
	public void setList(List<ToDoItem> newList);
}
