package toDo.exceptions;

public class DateParseException extends Exception 
{
	private static final long serialVersionUID = 1L;
	
	public DateParseException()
	{
		
	}
	
	public DateParseException(String msg)
	{
		super(msg);
	}

}
