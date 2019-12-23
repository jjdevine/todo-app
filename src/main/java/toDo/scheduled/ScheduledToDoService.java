package toDo.scheduled;

import toDo.persistence.PersistenceManager;
import toDo.persistence.PersistenceModel;
import toDo.scheduled.model.MonthlyScheduledTodo;
import toDo.scheduled.model.ScheduledTodo;
import toDo.scheduled.model.WeeklyScheduledTodo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScheduledToDoService {

    public static List<ScheduledTodo> loadAllScheduledToDos() throws IOException {

        List<ScheduledTodo> results = new ArrayList<>();
        List<PersistenceModel> persistenceModels = PersistenceManager.loadAllPersistenceModelsByType(ScheduledTodo.PERSISTENCE_TYPE);

        for(PersistenceModel model: persistenceModels) {
            switch (model.getSourceClass()) {
                case "toDo.scheduled.model.WeeklyScheduledTodo" :
                    results.add(new WeeklyScheduledTodo(model));
                    break;
                case "toDo.scheduled.model.MonthlyScheduledTodo":
                    results.add(new MonthlyScheduledTodo(model));
                    break;
            }
        }

        return results;
    }

    public static void deleteScheduledToDo(PersistenceModel model) throws IOException {
        PersistenceManager.deletePersistenceModel(model);
    }
}
