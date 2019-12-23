package toDo.scheduled.model;

import toDo.common.ToDoPriority;
import toDo.persistence.PersistenceModel;
import toDo.persistence.PersistenceUtils;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class ScheduledTodo {

    public static final String PERSISTENCE_TYPE = "ScheduledToDo";

    protected static final String PERSISTENCE_FREQUENCY = "frequency";
    protected static final String PERSISTENCE_START_DATE = "startDate";
    protected static final String PERSISTENCE_END_DATE = "endDate";
    protected static final String PERSISTENCE_TO_DO_PRIORITY = "toDoPriority";
    protected static final String PERSISTENCE_TITLE = "title";
    protected static final String PERSISTENCE_DESCRIPTION = "description";
    protected static final String PERSISTENCE_LAST_FIRE_DATE = "LastFireDate";
    protected static final String PERSISTENCE_NEXT_FIRE_DATE = "NextFireDate";

    protected String id;
    protected int frequency;
    protected Calendar startDate;
    protected Calendar endDate;
    protected ToDoPriority toDoPriority;
    protected String title;
    protected String description;
    protected Calendar lastFireDate;
    protected Calendar nextFireDate;


    public ScheduledTodo() {
        this.id = new Date().getTime()+"";
    }

    public ScheduledTodo(PersistenceModel model) {
        id = model.getId();

        Map<String, String> properties = model.getProperties();

        frequency = Integer.parseInt(properties.get(PERSISTENCE_FREQUENCY));
        startDate = PersistenceUtils.unmarshallCalendar(properties.get(PERSISTENCE_START_DATE));
        endDate = PersistenceUtils.unmarshallCalendar(properties.get(PERSISTENCE_END_DATE));
        toDoPriority = PersistenceUtils.unmarshallToDoPriority(properties.get(PERSISTENCE_TO_DO_PRIORITY));
        title = properties.get(PERSISTENCE_TITLE);
        description = properties.get(PERSISTENCE_DESCRIPTION);
        lastFireDate = PersistenceUtils.unmarshallCalendar(properties.get(PERSISTENCE_LAST_FIRE_DATE));
        nextFireDate = PersistenceUtils.unmarshallCalendar(properties.get(PERSISTENCE_NEXT_FIRE_DATE));
    }

    public String getId() {
        return id;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public void setStartDate(Calendar startDate) {
        this.startDate = startDate;
    }

    public Calendar getEndDate() {
        return endDate;
    }

    public void setEndDate(Calendar endDate) {
        this.endDate = endDate;
    }

    public ToDoPriority getToDoPriority() {
        return toDoPriority;
    }

    public void setToDoPriority(ToDoPriority toDoPriority) {
        this.toDoPriority = toDoPriority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Calendar getLastFireDate() {
        return lastFireDate;
    }

    public void setLastFireDate(Calendar lastFireDate) {
        this.lastFireDate = lastFireDate;
    }

    public Calendar getNextFireDate() {
        return nextFireDate;
    }

    public void setNextFireDate(Calendar nextFireDate) {
        this.nextFireDate = nextFireDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ScheduledTodo{");
        sb.append("id='").append(id).append('\'');
        sb.append(", frequency=").append(frequency);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", toDoPriority=").append(toDoPriority);
        sb.append(", title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public PersistenceModel toPersistenceModel() {
        PersistenceModel persistenceModel = new PersistenceModel();

        persistenceModel.setId(id);
        persistenceModel.setType(PERSISTENCE_TYPE);
        persistenceModel.setSourceClass(this.getClass().getName());

        persistenceModel.addProperty(PERSISTENCE_FREQUENCY, frequency+"");
        persistenceModel.addProperty(PERSISTENCE_START_DATE, PersistenceUtils.marshallCalendar(startDate));
        persistenceModel.addProperty(PERSISTENCE_END_DATE, PersistenceUtils.marshallCalendar(endDate));
        persistenceModel.addProperty(PERSISTENCE_TO_DO_PRIORITY, PersistenceUtils.marshallToDoPriority(toDoPriority));
        persistenceModel.addProperty(PERSISTENCE_TITLE, title);
        persistenceModel.addProperty(PERSISTENCE_DESCRIPTION, description);
        persistenceModel.addProperty(PERSISTENCE_LAST_FIRE_DATE, PersistenceUtils.marshallCalendar(lastFireDate));
        persistenceModel.addProperty(PERSISTENCE_NEXT_FIRE_DATE, PersistenceUtils.marshallCalendar(nextFireDate));

        return persistenceModel;
    }

    public abstract String getFrequencyDescription();

    public abstract String getScheduleDescription();

    public abstract boolean incrementNextFireDateIncludingToday();

    public abstract boolean incrementNextFireDateStartingTomorrow();
}
