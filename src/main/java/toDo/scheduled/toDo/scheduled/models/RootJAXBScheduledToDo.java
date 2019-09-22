package toDo.scheduled.toDo.scheduled.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@XmlRootElement(name = "root")
public class RootJAXBScheduledToDo {

    private List<ScheduledToDo> scheduledToDos;

    public RootJAXBScheduledToDo() {
        scheduledToDos = new ArrayList<>();
    }

    public List<ScheduledToDo> getScheduledToDos() {
        return scheduledToDos;
    }

    public void addScheduledToDos(ScheduledToDo... scheduledTodoArray) {
        scheduledToDos.addAll(Arrays.asList(scheduledTodoArray));
    }

    @XmlElementWrapper(name = "scheduledToDos")
    @XmlElements({
            @XmlElement(name="weekly", type = WeeklyScheduledTodo.class)
    })
    public void setScheduledToDos(List<ScheduledToDo> scheduledToDos) {
        this.scheduledToDos = scheduledToDos;
    }
}
