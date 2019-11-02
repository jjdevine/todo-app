package toDo.scheduled;

import toDo.persistence.PersistenceManager;
import toDo.persistence.PersistenceModel;
import toDo.scheduled.model.ScheduledTodo;
import toDo.scheduled.model.WeeklyScheduledTodo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ScheduledToDoService {

    public static void main(String[] args) throws Exception{
        System.out.println(new ScheduledToDoService().loadAllScheduledToDos());
    }

    public List<ScheduledTodo> loadAllScheduledToDos() throws IOException {

        List<ScheduledTodo> results = new ArrayList<>();
        List<PersistenceModel> persistenceModels = PersistenceManager.loadAllPersistenceModelsByType(ScheduledTodo.PERSISTENCE_TYPE);

        for(PersistenceModel model: persistenceModels) {
            switch (model.getSourceClass()) {
                case "toDo.scheduled.model.WeeklyScheduledTodo" :
                    results.add(new WeeklyScheduledTodo(model));
                    break;
                case "toDo.scheduled.model.MonthlyScheduledTodo":
                    break;
            }
        }

        return results;
    }
}
