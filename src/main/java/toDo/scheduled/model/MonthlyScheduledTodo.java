package toDo.scheduled.model;

import toDo.persistence.PersistenceModel;
import toDo.persistence.PersistenceUtils;

import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class MonthlyScheduledTodo extends ScheduledTodo {

    private static final String PERSISTENCE_SCHEDULED_DAYS = "scheduledDays";
    private Set<Integer> scheduledDays;

    public MonthlyScheduledTodo() {
        super();
    }

    public MonthlyScheduledTodo(PersistenceModel model) {
        super(model);

        Map<String, String> properties = model.getProperties();

        scheduledDays = Arrays.asList(properties.get(PERSISTENCE_SCHEDULED_DAYS)
                .split(","))
                .stream()
                .map(strVal -> Integer.parseInt(strVal))
                .collect(Collectors.toSet());
    }

    public Set<Integer> getScheduledDays() {
        return scheduledDays;
    }

    public void setScheduledDays(Set<Integer> scheduledDays) {
        this.scheduledDays = scheduledDays;
    }

    @Override
    public PersistenceModel toPersistenceModel() {
        PersistenceModel persistenceModel = super.toPersistenceModel();

        //convert days to comma separated list
        persistenceModel.addProperty(PERSISTENCE_SCHEDULED_DAYS,
                scheduledDays
                        .stream()
                        .map(dayInt -> dayInt+"")
                        .collect(Collectors.joining(",")));

        return persistenceModel;
    }



    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MonthlyScheduledTodo{");
        sb.append("scheduledDays=").append(scheduledDays);
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
        return "Every " + frequency + " month(s)";
    }

    @Override
    public String getScheduleDescription() {
        StringBuilder result = new StringBuilder("On days of month: ");

        List<Integer> dayList = new ArrayList<>();

        dayList.addAll(scheduledDays);
        dayList.sort((day1, day2) -> day1-day2);

        for(int x=0; x<dayList.size(); x++) {

            if(x != 0) {
                result.append(", ");
            }

            result.append(dayList.get(x));
        }

        return result.toString();
    }

    @Override
    public boolean incrementNextFireDateIncludingToday() {
return false;
    }

    @Override
    public boolean incrementNextFireDateStartingTomorrow() {
return false;
    }
}
