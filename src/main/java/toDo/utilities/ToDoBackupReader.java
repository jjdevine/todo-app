package toDo.utilities;

import java.io.File;
import java.util.*;

import toDo.data.FileAndText;

public class ToDoBackupReader 
{
	private String strFile;
	
	public ToDoBackupReader(String strFile)
	{
		this.strFile = strFile;
	}
	
	/**
	 * method specifically written to decode the amalgated backupfiles in the
	 * to do application
	 * @return
	 */
	public List<FileAndText> getFiles()
	{
		List<FileAndText> fileList = new ArrayList<FileAndText>();
		
		String[] strArr = strFile.split("\n");
		
		File currentFile = null;
		StringBuilder thisFileStr = null;
		
		for (int lineNum = 0; lineNum < strArr.length; lineNum++)
		{
			/*
			 * if line is start of file marker
			 */
			if(strArr[lineNum].length() > 8 && strArr[lineNum].substring(0,8).equalsIgnoreCase("<<file>>"))
			{
				currentFile = new File(strArr[lineNum].split("=")[1]);
				thisFileStr = new StringBuilder("");
				
				lineNum++; //move to next line
				
				/*
				 * while not end of file marker
				 */
				while(!strArr[lineNum].trim().equalsIgnoreCase("<<END OF FILE>>"))
				{
					thisFileStr.append(strArr[lineNum] + "\n");
					lineNum++;
				}
				
				//add completed file to list
				fileList.add(new FileAndText(currentFile, thisFileStr.toString()));
			}		
		}
		
		return fileList;
	}
	
	
}
