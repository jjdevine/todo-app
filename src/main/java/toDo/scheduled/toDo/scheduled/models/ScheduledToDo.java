package toDo.scheduled.toDo.scheduled.models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Calendar;
import java.util.Date;

@XmlRootElement
public abstract class ScheduledToDo {

    private Calendar endDate;

    private Calendar lastElapsed;

    public ScheduledToDo() {
        lastElapsed = Calendar.getInstance();
    }

    public Calendar getEndDate() {
        return endDate;
    }

    @XmlElement
    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public Calendar getLastElapsed() {
        return lastElapsed;
    }

    @XmlElement
    public void setLastElapsed(Calendar lastElapsed) {
        this.lastElapsed = lastElapsed;
    }
}
