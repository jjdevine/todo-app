package toDo.gui.alert;

import toDo.alert.Alert;
import toDo.alert.MessageAlert;
import toDo.alert.PriorityAlert;
import toDo.data.ToDoItem;
import toDo.utilities.Global;
import toDo.utilities.GuiUtils;
import toDo.utilities.ToDoFileIO;
import toDo.utilities.ToDoUtilities;
import toDo.utilities.ToDoXMLDecoder;
import java.io.File;
import java.util.Collections;
import java.util.List;

public class AlertManager {

    private File alertFile = new File("./todo_alerts.data");

    private List<Alert> alertList;

    private static AlertManager alertManager;

    public AlertManager() {

        refreshAlertsFromFile();
    }

    private synchronized void refreshAlertsFromFile() {
        alertList = new ToDoXMLDecoder(ToDoFileIO.loadFile(alertFile)).decodeXMLasAlertList();
        Collections.sort(alertList);
    }

    /**
     * write the current alert list to file
     */
    public synchronized void saveAlertsToFile()
    {
        ToDoFileIO.saveAlertsAsFile(alertFile, alertList);
    }

    public List<Alert> getAlertList (){
        return alertList;
    }

    public static synchronized AlertManager getInstance() {
        if(alertManager == null) {
            alertManager = new AlertManager();
        }

        return alertManager;
    }

    public void removeAlertsForTodoItem(int todoItemId) {
        alertList.removeIf(item -> item.getToDoID() == todoItemId);
        saveAlertsToFile();
    }


    public void deleteAlert(Alert alert) {
        alertList.remove(alert);
        saveAlertsToFile();
    }

    public void fireAlert(Alert alert) {
        ToDoItem item = ToDoUtilities.getToDoByID(alert.getToDoID());
        boolean notify = alert.isNotifier();

        if(alert instanceof PriorityAlert)
        {
            PriorityAlert pAlert = (PriorityAlert)alert;
            int priority = pAlert.getPriority();
            item.setPriority(priority);
            item.addToLog(ToDoItem.LOG_ALERT, "Priority set to : " + ToDoItem.priorityAsString(priority));
            if(notify)
            {
                GuiUtils.addInfoMessage("To Do '" + item.getDescription() + "' changed to priority " + ToDoItem.priorityAsString(priority), true);
            }
            Global.getMainGui().refreshToDoDisplay();

        }
        else if(alert instanceof MessageAlert)
        {
            MessageAlert mAlert = (MessageAlert)alert;
            String message = mAlert.getMessage();
            item.addToLog(ToDoItem.LOG_ALERT, message);
            if(notify)
            {
                GuiUtils.addInfoMessage("Message alert for To Do '" + item.getDescription() + "'.\n\n " + message, true);
            }
        }
    }

    public void addAlert(Alert alert) {
        alertList.add(alert);
        Collections.sort(alertList);
        saveAlertsToFile();
    }
}
