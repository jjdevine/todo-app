package toDo.go;

import toDo.gui.main.MainGui;
import toDo.utilities.Global;

public class RunTodo {

	
	public static void main(String[] args) 
	{
		MainGui mainGui = new MainGui();
		Global.setMainGui(mainGui);
	}

}
