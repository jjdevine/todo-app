package toDo.data;

import java.io.File;

public class FileAndText 
{
	private File file;
	private String text;
	
	public FileAndText(File f, String s)
	{
		file = f;
		text = s;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
