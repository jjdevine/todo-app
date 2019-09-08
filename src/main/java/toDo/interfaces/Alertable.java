package toDo.interfaces;

import java.util.List;

import toDo.alert.*;

public interface Alertable 
{
	public void addAlert(Alert a);
	public List<Alert> getListAlerts();
	public void removeAlert(Alert a);
	public void removeAllAlertsForID(int id);
}
