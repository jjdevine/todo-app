package toDo.scheduled;

import toDo.data.ToDoItem;
import toDo.persistence.PersistenceManager;
import toDo.persistence.PersistenceModel;
import toDo.scheduled.model.MonthlyScheduledTodo;
import toDo.scheduled.model.ScheduledTodo;
import toDo.scheduled.model.WeeklyScheduledTodo;
import toDo.utilities.GuiUtils;
import toDo.utilities.ToDoUtilities;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.TimerTask;

public class ScheduledToDoTimerTask extends TimerTask {

    @Override
    public void run() {

        List<PersistenceModel> persistenceModels = null;

        try {
            persistenceModels = PersistenceManager.loadAllPersistenceModelsByType(ScheduledTodo.PERSISTENCE_TYPE);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        for(PersistenceModel model: persistenceModels) {
            switch (model.getSourceClass()) {
                case "toDo.scheduled.model.WeeklyScheduledTodo" :
                        processScheduledToDo(new WeeklyScheduledTodo(model));
                    break;
                case "toDo.scheduled.model.MonthlyScheduledTodo":
                        processScheduledToDo(new MonthlyScheduledTodo(model));
                    break;
            }
        }
    }

    private void processScheduledToDo(ScheduledTodo scheduledTodo) {

        if(scheduledTodo.getNextFireDate() == null) {
            return; //shouldn't happen but ignore.
        }

        if(scheduledTodo.getNextFireDate().before(Calendar.getInstance())) {
            ToDoItem toDoItem = new ToDoItem(scheduledTodo.getToDoPriority().getIndex(), scheduledTodo.getTitle());
            toDoItem.addToLog(ToDoItem.LOG_AUDIT, "To Do item generated from schedule");
            toDoItem.addToLog(ToDoItem.LOG_AUDIT, toDoItem.getDescription());
            ToDoUtilities.createNewToDoItem(toDoItem);

            updatePersistenceModelAfterFire(scheduledTodo);
            GuiUtils.showInformation("Created new Scheduled ToDo: " + scheduledTodo.getTitle(), "Scheduled ToDo Created");
            return;
        }
    }

    private void updatePersistenceModelAfterFire(ScheduledTodo scheduledTodo) {
        try {
            if (!scheduledTodo.incrementNextFireDateStartingTomorrow()) {
                PersistenceManager.deletePersistenceModel(scheduledTodo.toPersistenceModel());
                return;
            }

            PersistenceManager.persist(scheduledTodo.toPersistenceModel());
        } catch (IOException ex) {
            GuiUtils.showError("Error updating scheduled todo - check the logs");
            ex.printStackTrace();
        }
    }
}
