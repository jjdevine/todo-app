package toDo.scheduled;

import toDo.data.ToDoItem;
import toDo.data.ToDoSchedule;
import toDo.gui.customComponents.ToDoHolder;
import toDo.gui.main.MainGui;
import toDo.utilities.GuiUtils;

import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

public class AutoEscalationTimerTask extends TimerTask {

    private MainGui gui;

    public AutoEscalationTimerTask(MainGui gui) {
        this.gui = gui;
    }

    @Override
    public void run() {

        List<ToDoHolder> toDoList = gui.getToDoList();

        for(ToDoHolder holder: toDoList) {
            ToDoItem item = holder.getToDoItem();
            ToDoSchedule schedule = item.getSchedule();

            if(schedule == null) {
                continue;
            }

            if(schedule.getNextDueDate().before(Calendar.getInstance())) {
                boolean shouldNotify = applySchedule(item);
                schedule.rollToNextDueDate();
                gui.refreshToDoDisplay();
                if(shouldNotify) {
                    GuiUtils.showInformation("To do item " + item.getDescription() + " was automatically prioritised to " + item.priorityAsString(), "Auto Escalation");
                }
            }
        }
    }

    private boolean applySchedule(ToDoItem item) {

        ToDoSchedule schedule = item.getSchedule();
        boolean shouldNotify = schedule.isNotify();

        ToDoSchedule.TargetPriority targetPriority = schedule.getTargetPriority();

        switch (targetPriority) {
            case URGENT:
                item.setPriority(ToDoItem.PRIORITY_URGENT);
                item.addToLog(ToDoItem.LOG_AUDIT, "Auto Escalated to " + item.priorityAsString());
                break;
            case HIGH:
                item.setPriority(ToDoItem.PRIORITY_HIGH);
                item.addToLog(ToDoItem.LOG_AUDIT, "Auto Escalated to " + item.priorityAsString());
                break;
            case MEDIUM:
                item.setPriority(ToDoItem.PRIORITY_MEDIUM);
                item.addToLog(ToDoItem.LOG_AUDIT, "Auto Escalated to " + item.priorityAsString());
                break;
            case NEXT_PRIORITY:
                int currentPriority = item.getPriority();
                if(currentPriority > 0) {
                    item.setPriority(currentPriority-1);
                    item.addToLog(ToDoItem.LOG_AUDIT, "Auto Escalated to " + item.priorityAsString());
                } else {
                    shouldNotify = false;
                }
                break;
        }
        return shouldNotify;
    }
}
