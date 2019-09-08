package toDo.fileFilters;

import java.io.File; //file handling package

public class dataFileFilter extends javax.swing.filechooser.FileFilter   //custom file filter class
{
	public boolean accept(File f)	//overwrite abstract method accept
	{
		if (f.isDirectory())	//if file is directory
		{
     		 return true;	//continue to browse files
		}

		String extension = getExtension(f);	//get file extension type

 		if (extension.equals("data"))	//if data file
		{
	        	return true;  //make file visible to user
		}

		return false;	//default
	}

	public String getDescription()	//shown in filetype combobox
	{
		return ".data files";	//message to show
	}

        private String getExtension(File f)	//method to return file extension in lowercase
        {
        	String s = f.getName();	//complete filename
        	int i = s.lastIndexOf('.');	//find last . in filename
    		if (i > 0 &&  i < s.length() - 1)	//if valid filename
		{
      			return s.substring(i+1).toLowerCase();	//return file extension
		}
    	return "";	//default
	}

}
