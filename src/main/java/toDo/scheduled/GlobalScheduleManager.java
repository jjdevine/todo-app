package toDo.scheduled;

import toDo.utilities.GuiUtils;
import toDo.utilities.ToDoUtilities;

import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;

public class GlobalScheduleManager extends TimerTask {

    private List<TodoScheduledProcess> processes;

    public GlobalScheduleManager(TodoScheduledProcess... processes) {
        this.processes = Arrays.asList(processes);
    }

    @Override
    public void run() {
        for(TodoScheduledProcess process: processes) {
            try {
                System.out.println("Running process - " + process.getClass());
                process.run();
            } catch (Exception ex) {
                ToDoUtilities.handleException(ex);
            }
        }
    }


}
