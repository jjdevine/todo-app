package toDo.scheduled;

import toDo.persistence.PersistenceManager;
import toDo.persistence.PersistenceModel;
import toDo.scheduled.model.MonthlyScheduledTodo;
import toDo.scheduled.model.ScheduledTodo;
import toDo.scheduled.model.WeeklyScheduledTodo;

import java.io.IOException;
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
                 //   new WeeklyScheduledTodo(model).;
                    break;
                case "toDo.scheduled.model.MonthlyScheduledTodo":
                    break;
            }
        }
        //TODO: implement this
    }
}
