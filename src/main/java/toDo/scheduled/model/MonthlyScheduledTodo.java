package toDo.scheduled.model;

import toDo.persistence.PersistenceModel;

import java.util.Set;
import java.util.stream.Collectors;

public class MonthlyScheduledTodo extends ScheduledTodo {

    private static final String PERSISTENCE_SCHEDULED_DAYS = "scheduledDays";
    private Set<Integer> scheduledDays;

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
}
