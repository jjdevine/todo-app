package toDo.scheduled.toDo.scheduled.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

@XmlRootElement
public class WeeklyScheduledTodo extends ScheduledToDo{

    public enum DaysOfWeek {
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        SUNDAY
    }

    private EnumSet<DaysOfWeek> daysOfWeek;

    @XmlElementWrapper(name = "daysOfWee1k")
    @XmlElement
    public List<DaysOfWeek> getDaysOfWeek() {
        List<DaysOfWeek> listDaysOfWeek = new ArrayList<>();
        listDaysOfWeek.addAll(daysOfWeek);
        return listDaysOfWeek;
    }

    //TODO: fix marshalling of days of week list
    //TODO add option for "every {x} weeks"

    public void setDaysOfWeek(EnumSet<DaysOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }
}
