package toDo.scheduled.model;

import toDo.persistence.PersistenceModel;
import toDo.persistence.PersistenceUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeeklyScheduledTodo extends ScheduledTodo {

    private static final String PROPERTY_MONDAY = "MONDAY";
    private static final String PROPERTY_TUESDAY = "TUESDAY";
    private static final String PROPERTY_WEDNESDAY = "WEDNESDAY";
    private static final String PROPERTY_THURSDAY = "THURSDAY";
    private static final String PROPERTY_FRIDAY = "FRIDAY";
    private static final String PROPERTY_SATURDAY = "SATURDAY";
    private static final String PROPERTY_SUNDAY = "SUNDAY";

    private boolean onMonday;
    private boolean onTuesday;
    private boolean onWednesday;
    private boolean onThursday;
    private boolean onFriday;
    private boolean onSaturday;
    private boolean onSunday;

    public boolean isOnMonday() {
        return onMonday;
    }

    public void setOnMonday(boolean onMonday) {
        this.onMonday = onMonday;
    }

    public boolean isOnTuesday() {
        return onTuesday;
    }

    public void setOnTuesday(boolean onTuesday) {
        this.onTuesday = onTuesday;
    }

    public boolean isOnWednesday() {
        return onWednesday;
    }

    public void setOnWednesday(boolean onWednesday) {
        this.onWednesday = onWednesday;
    }

    public boolean isOnThursday() {
        return onThursday;
    }

    public void setOnThursday(boolean onThursday) {
        this.onThursday = onThursday;
    }

    public boolean isOnFriday() {
        return onFriday;
    }

    public void setOnFriday(boolean onFriday) {
        this.onFriday = onFriday;
    }

    public boolean isOnSaturday() {
        return onSaturday;
    }

    public void setOnSaturday(boolean onSaturday) {
        this.onSaturday = onSaturday;
    }

    public boolean isOnSunday() {
        return onSunday;
    }

    public void setOnSunday(boolean onSunday) {
        this.onSunday = onSunday;
    }

    public WeeklyScheduledTodo() {
        super();
    }

    public WeeklyScheduledTodo(PersistenceModel model) {
        super(model);

        Map<String, String> properties = model.getProperties();

        onMonday = PersistenceUtils.unmarshallBoolean(properties.get(PROPERTY_MONDAY));
        onTuesday = PersistenceUtils.unmarshallBoolean(properties.get(PROPERTY_TUESDAY));
        onWednesday = PersistenceUtils.unmarshallBoolean(properties.get(PROPERTY_WEDNESDAY));
        onThursday = PersistenceUtils.unmarshallBoolean(properties.get(PROPERTY_THURSDAY));
        onFriday = PersistenceUtils.unmarshallBoolean(properties.get(PROPERTY_FRIDAY));
        onSaturday = PersistenceUtils.unmarshallBoolean(properties.get(PROPERTY_SATURDAY));
        onSunday = PersistenceUtils.unmarshallBoolean(properties.get(PROPERTY_SUNDAY));


        //TODO: implement same for Monthly Todo, and then insert into scheduledToDoService.java
    }

    @Override
    public PersistenceModel toPersistenceModel() {
        PersistenceModel persistenceModel = super.toPersistenceModel();

        persistenceModel.addProperty(PROPERTY_MONDAY, PersistenceUtils.marshallBoolean(onMonday));
        persistenceModel.addProperty(PROPERTY_TUESDAY, PersistenceUtils.marshallBoolean(onTuesday));
        persistenceModel.addProperty(PROPERTY_WEDNESDAY, PersistenceUtils.marshallBoolean(onWednesday));
        persistenceModel.addProperty(PROPERTY_THURSDAY, PersistenceUtils.marshallBoolean(onThursday));
        persistenceModel.addProperty(PROPERTY_FRIDAY, PersistenceUtils.marshallBoolean(onFriday));
        persistenceModel.addProperty(PROPERTY_SATURDAY, PersistenceUtils.marshallBoolean(onSaturday));
        persistenceModel.addProperty(PROPERTY_SUNDAY, PersistenceUtils.marshallBoolean(onSunday));

        return persistenceModel;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WeeklyScheduledTodo{");
        sb.append("onMonday=").append(onMonday);
        sb.append(", onTuesday=").append(onTuesday);
        sb.append(", onWednesday=").append(onWednesday);
        sb.append(", onThursday=").append(onThursday);
        sb.append(", onFriday=").append(onFriday);
        sb.append(", onSaturday=").append(onSaturday);
        sb.append(", onSunday=").append(onSunday);
        sb.append(", id='").append(id).append('\'');
        sb.append(", frequency=").append(frequency);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", toDoPriority=").append(toDoPriority);
        sb.append(", title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public String getFrequencyDescription() {
        return "Every " + frequency + " week(s)";
    }

    @Override
    public String getScheduleDescription() {
        StringBuilder result = new StringBuilder("On ");

        List<String> days = new ArrayList<>();

        if(onMonday) {
            days.add("Monday");
        }
        if(onTuesday) {
            days.add("Tuesday");
        }
        if(onWednesday) {
            days.add("Wednesday");
        }
        if(onThursday) {
            days.add("Thursday");
        }
        if(onFriday) {
            days.add("Friday");
        }
        if(onSaturday) {
            days.add("Saturday");
        }
        if(onSunday) {
            days.add("Sunday");
        }

        for(int x=0; x<days.size(); x++) {

            if(x != 0) {
                result.append(", ");
            }

            result.append(days.get(x));
        }

        return result.toString();
    }
}
