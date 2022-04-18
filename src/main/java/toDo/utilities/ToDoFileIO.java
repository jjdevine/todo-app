package toDo.utilities;

import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

import toDo.alert.Alert;
import toDo.data.FileAndText;
import toDo.data.ToDoItem;
import toDo.gui.customComponents.ToDoArchivedHolder;
import toDo.gui.customComponents.ToDoHolder;

public class ToDoFileIO 
{
	public static void saveToDosAsFile(File f, List<ToDoItem> toDoList)
	{
		if (f!=null)
		{
			try
			{
				PrintWriter out = new PrintWriter(new FileOutputStream(f),true);	//open file
				out.println(new ToDoXMLEncoder(toDoList).encodeXMLToDoFile(true));	//write to file
				out.close();	//close file
			}
			catch(IOException ex)
			{
				GuiUtils.addInfoMessage("Problem saving to file system : " + ex.getMessage(), true);
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * append a single item to a file
	 * @param f
	 * @param item
	 */
	public static void appendToDoToFile(File f, ToDoItem item)
	{
		if (f!= null)
		{
			/*
			 * easiest to just put the one item in a list
			 */
			
			List<ToDoItem> list = new ArrayList<ToDoItem>();
			list.add(item);
			
			if(f.exists())
			{
				try
				{
					
					 
					PrintWriter out = new PrintWriter(new FileOutputStream(f,true),true);
					out.println(new ToDoXMLEncoder(list).encodeXMLToDoFile(false));	//write to file
					out.close();	//close file
				}
				catch(Exception ex)
				{
					
				}
			}
			else //doesnt exit, need to create it
			{
				//need to create full file, so use other method
				saveToDosAsFile(f, list);
			}
		}
	}
	
	public static List<ToDoHolder> loadToDosAsHolderList(File f)
	{
		String strFile = loadFile(f);
		
		List<ToDoItem> itemList = new ToDoXMLDecoder(strFile).decodeXMLasToDoItemList();	
		
		List<ToDoHolder> holderList = new ArrayList<ToDoHolder>();
		
		Iterator i = itemList.iterator();
		
		while(i.hasNext())
		{
			holderList.add(new ToDoHolder((ToDoItem)i.next()));
		}
		
		return holderList;	
	}
	
	public static List<ToDoArchivedHolder> loadToDosAsArchivedHolderList(File f)
	{
		String strFile = loadFile(f);
		
		List<ToDoItem> itemList = new ToDoXMLDecoder(strFile).decodeXMLasToDoItemList();	
		
		List<ToDoArchivedHolder> holderList = new ArrayList<ToDoArchivedHolder>();
		
		Iterator i = itemList.iterator();
		
		while(i.hasNext())
		{
			holderList.add(new ToDoArchivedHolder((ToDoItem)i.next()));
		}
		
		return holderList;
	}
	
	
	
	/**
	 * converts the supplied file into a single string
	 * @param filename
	 * @return
	 */
	public static String loadFile(File filename)	//method to load any file
	{
		if (filename.exists())
		{
			StringBuilder fileAsString = new StringBuilder("");
			String currentLine="";
			try
			{
				FileReader in = new FileReader(filename);	//new filereader
				BufferedReader reader = new BufferedReader(in);	//buffered reader
				
				while((currentLine=reader.readLine())!=null)	//while not at end of file
				{
					fileAsString.append(currentLine +"\n"); 	//add current line to string gathered so far
				}
				in.close();
				reader.close();
			}
			catch(Exception ex)	//error loading file
			{
				GuiUtils.addInfoMessage("Problem loading from file system : " + ex.getMessage(), true);
				ex.printStackTrace();
			}
			
		return fileAsString.toString();	//return loaded file
		}
		else
		{
			return "";
		}
	}
	
	/**
	 * since for memory reasons the user can store extra archives, this method 
	 * looks for other saved archives, it is assumed that other archives are
	 * saved under the format "past_archive_n.data" where n is a number.  
	 */
	public static List<File> getExtraArchives()
	{
		File[] fileArr = new File(".").listFiles();
		
		List<File> fileList = new ArrayList<File>();
		
		for(File f: fileArr)
		{
			String fileStr = f.toString().substring(2);
			
			if (fileStr.length() > 16 && fileStr.substring(0,13).equalsIgnoreCase("past_archive_"))
			{
				if(fileStr.substring(fileStr.length()-5, fileStr.length()).equalsIgnoreCase(".data"))
				{
					fileList.add(f);
				}
			}
			
		}
		
		return fileList;
	}
	
	/**
	 * gets dates as string associated with start and end of a supplied archive 
	 * file. the filename assumed for extra info files is <f>.ini
	 * 
	 * of the return value, array position 0 is the startdate, array position
	 * 1 is the end date
	 * @param f
	 * @return
	 */
	public static String[] getExtraInfo(File f)
	{
		String[] dateArr = {"unknown","unknown"};
		
		File iniFile = new File(f+".ini");
		
		if(iniFile.exists()) //if can find ini file for this archive
		{
			String fileStr = loadFile(iniFile);
			String[] lines = fileStr.split("\n");
			
			for(String thisLine: lines) //pick out data
			{
				if(thisLine.length() > 11 && thisLine.substring(0,11).equalsIgnoreCase("start date="))
				{
					dateArr[0] = thisLine.substring(11);
				}
				if(thisLine.length() > 9 && thisLine.substring(0,9).equalsIgnoreCase("end date="))
				{
					dateArr[1] = thisLine.substring(9);
				}
				
			}
		}
		
		return dateArr;
	}
	
	/**
	 * returns the next usable archive filename not in use
	 * @return
	 */
	public static File getNextArchiveFileName()
	{

		for(int x=1; x<100000; x++) //loop should never expire
		{
			File f = new File("past_archive_" + x +".data");
			
			if(!f.exists())//this file is not in use
			{
				return f;
			}
		}
		
		return null;

	}
	
	public static void clearFile(File f)
	{
		if (f!=null)
		{
			try
			{
				PrintWriter out = new PrintWriter(new FileOutputStream(f),false);	//open file
				out.print("");
				out.close();
			}
			catch(IOException ex)
			{
				GuiUtils.addInfoMessage("Problem clearing file : " + f + " " + ex.getMessage(),true);
				ex.printStackTrace();
			}
		}
	}
	
	public static void createIniFile(File f, String... params)
	{
		if (f!=null)
		{
			try
			{
				PrintWriter out = new PrintWriter(new FileOutputStream(f),true);	//open file
				for(String str:params)
				{
					out.println(str);	//write to file
				}
				out.close();	//close file
			}
			catch(IOException ex)
			{
				GuiUtils.addInfoMessage("Problem writing ini file : " + ex.getMessage(), true);
				ex.printStackTrace();
			}
		}
	}

	public static void amalgamateFiles(File file, List<File> filesToSave) 
	{
		if(file!=null)
		{
			try
			{
				PrintWriter out = new PrintWriter(new FileOutputStream(file),true);
				
				Iterator<File> i = filesToSave.iterator();
				
				while(i.hasNext())
				{
					File thisFile = i.next();
					
					if (thisFile != null)
					{
						String fileStr = loadFile(thisFile);
						out.println("<<File>>=" + thisFile);
						out.println(fileStr);
						out.println("<<END OF FILE>>");
					}
					
					
				}
				out.close();
			}
			catch(IOException ex)
			{
				GuiUtils.addInfoMessage("Problem writing backup file : " + ex.getMessage(), true);
				ex.printStackTrace();
			}
			
		}
		
	}
	
	public static List<File> getIniFiles()
	{
		List<File> iniFiles = new ArrayList<File>();
			
		File[] fileArr = new File(".").listFiles();
		
		for(File f: fileArr)
		{
			String fileStr = f.toString().substring(2);
			
			if (fileStr.length() > 16 && fileStr.substring(0,13).equalsIgnoreCase("past_archive_"))
			{
				if(fileStr.substring(fileStr.length()-9, fileStr.length()).equalsIgnoreCase(".data.ini"))
				{
					iniFiles.add(f);
				}
			}
			
		}
	
		return iniFiles;
	}
	
	public static void writeFiles(List<FileAndText> fileList)
	{
		Iterator<FileAndText> i = fileList.iterator();
		
		while(i.hasNext())
		{
			FileAndText fileAndText = i.next();
			
			File f = fileAndText.getFile();
			String str = fileAndText.getText();
			
			try
			{
				PrintWriter out = new PrintWriter(new FileOutputStream(f),true);	//open file
				out.println(str);	//write to file
				out.close();	//close file
			}
			catch(IOException ex)
			{
				GuiUtils.addInfoMessage("Problem writing backed up file : " + f + " -" + ex.getMessage(), true);
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * destroy all files associated with todo list in this directory
	 * @param currentFile
	 * @param archive
	 */
	public static void destroyToDoFiles(File currentFile, File archive)
	{
		List<File> list = new ArrayList<File>();
		list.add(currentFile);
		list.add(archive);
		
		try
		{
			if(currentFile.exists())
			{
				currentFile.delete();
			}
			if(archive.exists())
			{
				archive.delete();
			}
		
			list = getExtraArchives();
		
			Iterator<File> i = list.iterator();
			
			while(i.hasNext())
			{
				File nextFile = i.next();
				
				if(nextFile.exists())
				{
					nextFile.delete();
				}
			}
			
			list = getIniFiles();
			
			i = list.iterator();
			
			while(i.hasNext())
			{
				File nextFile = i.next();
				
				if(nextFile.exists())
				{
					nextFile.delete();
				}
			}
		}
		catch(Exception ex)
		{
			GuiUtils.addInfoMessage("Problem removing old files " + ex.getMessage(), true);
		}
	}
	
	public static void saveAlertsAsFile(File f, List<Alert> listAlerts)
	{
		if (f!=null)
		{
			try
			{
				PrintWriter out = new PrintWriter(new FileOutputStream(f),true);	//open file
				out.println(new ToDoXMLEncoder().encodeXMLAlertFile(listAlerts));	//write to file
				out.close();	//close file
			}
			catch(IOException ex)
			{
				GuiUtils.addInfoMessage("Problem saving to file system : " + ex.getMessage(), true);
				ex.printStackTrace();
			}
		}
	}
	
	public static void appendAlertToFile(File f, Alert a)
	{
		if (f!= null)
		{
			/*
			 * easiest to just put the one item in a list
			 */
			
			List<Alert> list = new ArrayList<Alert>();
			list.add(a);
			
			if(f.exists())
			{
				try
				{
					
					 
					PrintWriter out = new PrintWriter(new FileOutputStream(f,true),true);//append
					out.println(new ToDoXMLEncoder().encodeXMLAlertFile(list));	//write to file
					out.close();	//close file
				}
				catch(Exception ex)
				{
					
				}
			}
			else //doesnt exit, need to create it
			{
				//need to create full file, so use other method
				saveAlertsAsFile(f, list);
			}
		}
	}
	
	/**
	 * creates a director if it doesnt already exist
	 * @param dir
	 */
	public static void createDir(File dir)
	{
		if(!dir.exists())
		{
			dir.mkdir();
		}
	}
	
}
