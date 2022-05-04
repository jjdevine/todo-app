package toDo.scheduled;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

import toDo.gui.main.MainGui;
import toDo.utilities.Global;
import toDo.utilities.ToDoFileIO;
import toDo.utilities.ToDoUtilities;

public class AutoSaveScheduledProcess implements TodoScheduledProcess
{
	private MainGui gui;

	private LocalDateTime lastSavePoint;

	public static final String SAVEDIR = "autosave";
	public AutoSaveScheduledProcess()
	{
		gui = Global.getMainGui();
		lastSavePoint = LocalDateTime.now();
	}
	
	@Override
	public void run() 
	{
		LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);

		System.out.println("Last save point: " + lastSavePoint.toString());
		System.out.println("One Hour ago: " + oneHourAgo);

		//only save if more than an hour since the last save
		if(lastSavePoint.isAfter(oneHourAgo)) {
			System.out.println("No Save required");
			return;
		}



		//create save directory
		File saveDir = new File(SAVEDIR);
		ToDoFileIO.createDir(saveDir);
		
		List<File> autoSaveFiles = ToDoUtilities.filterAutoSaves(saveDir.listFiles());
		
		for(File f: autoSaveFiles)
		{
			System.out.println(f);
		}
		
		gui.createBackup(new File(SAVEDIR + "/" + generateFileEnding()), false);
		lastSavePoint = LocalDateTime.now();
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
