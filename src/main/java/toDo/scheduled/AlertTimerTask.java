package toDo.scheduled;

import java.util.*;

public class AlertTimerTask extends TimerTask 
{
	AlertProcessor processor;
	
	public AlertTimerTask(AlertProcessor processor)
	{
		this.processor = processor;
	}
	
	@Override
	public void run() 
	{
		processor.action();
	}
}
