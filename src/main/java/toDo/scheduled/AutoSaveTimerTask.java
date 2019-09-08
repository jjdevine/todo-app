package toDo.scheduled;

import java.io.File;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

import toDo.gui.main.MainGui;
import toDo.utilities.ToDoFileIO;
import toDo.utilities.ToDoUtilities;

public class AutoSaveTimerTask extends TimerTask 
{
	MainGui gui;
	public static final String SAVEDIR = "autosave";
	public AutoSaveTimerTask(MainGui gui)
	{
		this.gui = gui;
	}
	
	@Override
	public void run() 
	{
		//create save directory
		File saveDir = new File(SAVEDIR);
		ToDoFileIO.createDir(saveDir);
		
		List<File> autoSaveFiles = ToDoUtilities.filterAutoSaves(saveDir.listFiles());
		
		for(File f: autoSaveFiles)
		{
			System.out.println(f);
		}
		System.out.println("********");
		
		gui.createBackup(new File(SAVEDIR + "/" + generateFileEnding()), false);
	}
	
	private String generateFileEnding()
	{
		Calendar c = Calendar.getInstance();
		
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		String strYear = year +"";
		String strMonth = ++month<10 ? "0"+month : ""+month ;
		String strDay = day<10 ? "0"+day : ""+day;
		String strHour = hour<10 ? "0"+hour : ""+hour;
		String strMinute = minute<10 ? "0"+minute : ""+minute;
		
		return strYear + "_" + strMonth + "_" + strDay + "_" + strHour + "_" + strMinute;
	}
}
