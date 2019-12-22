package toDo.scheduled.model;

import toDo.persistence.PersistenceModel;
import toDo.persistence.PersistenceUtils;
import toDo.utilities.ToDoUtilities;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
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
        sb.append(", startDate=").append(ToDoUtilities.formatCalendar(startDate));
        sb.append(", endDate=").append(ToDoUtilities.formatCalendar(endDate));
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

    @Override
    public void incrementNextFireDate() {

        Calendar startDate = getNextFireDate();

        if(startDate == null) {
            startDate = getStartDate();
            if(isDueToFireOnDate(ToDoUtilities.convertCalendarToLocalDate(startDate))) {

            }
        }
//TODO finish this


    }

    private boolean isDueToFireOnDate(LocalDate date) {

        LocalDate startDate = ToDoUtilities.convertCalendarToLocalDate(getStartDate());

        if (date.isBefore(startDate)) {
            return false;
        }

        if(!matchesScheduledDayOfWeek(date)) {
            return false;
        }

        if(!matchesScheduledWeek(date)) {
            return false;
        }

        return true;
    }

    /**
     * for testing
     * @param args
     */
    public static void main(String[] args) {
        WeeklyScheduledTodo todo = new WeeklyScheduledTodo();

        Calendar startDate = Calendar.getInstance();
        startDate.roll(Calendar.DAY_OF_YEAR, -10);

        todo.setStartDate(startDate);
        todo.setFrequency(4);
        todo.setOnSaturday(true);
        todo.setOnMonday(true);
        todo.setOnSunday(true);

        System.out.println(todo);


        for(int days = -50; days<51; days++) {
            LocalDate comparedTo = LocalDate.now().plusDays(days);
            System.out.println("compared to " + comparedTo + " : " + todo.isDueToFireOnDate(comparedTo));
        }

        todo.setStartDate(Calendar.getInstance());
    }

    private boolean matchesScheduledWeek(LocalDate date) {
        LocalDate startDate = ToDoUtilities.convertCalendarToLocalDate(getStartDate());
        LocalDate dateToCheck = startDate;

        while(!fallsInALaterWeek(dateToCheck, date)) {

            if(inSameWeek(dateToCheck, date)) {
                return true;
            }

            dateToCheck = dateToCheck.plusWeeks(getFrequency());
        }

        return false;
    }

    private boolean fallsInALaterWeek(LocalDate date, LocalDate dateInTargetWeek) {
        return rollBackToMonday(date).isAfter(rollBackToMonday(dateInTargetWeek));
    }

    private boolean inSameWeek(LocalDate date1, LocalDate date2) {
        /*
        get the start and end date of the week date 1 falls in, and check if date 2 is between those dates
         */

        LocalDate date1Monday = rollBackToMonday(date1);
        LocalDate date1Sunday = rollForwardToSunday(date1);

        return date2.isEqual(date1Monday)
                || date2.isEqual(date1Sunday)
                || (date2.isBefore(date1Sunday) && date2.isAfter(date1Monday));
    }

    private LocalDate rollBackToMonday(LocalDate date) {
        while(date.getDayOfWeek() != DayOfWeek.MONDAY) {
            date = date.minusDays(1);
        }

        return date;
    }

    private LocalDate rollForwardToSunday(LocalDate date) {
        while(date.getDayOfWeek() != DayOfWeek.SUNDAY) {
            date = date.plusDays(1);
        }

        return date;
    }

    private boolean matchesScheduledDayOfWeek(LocalDate date) {
        switch(date.getDayOfWeek()) {
            case MONDAY:
                return isOnMonday();
            case TUESDAY:
                return isOnTuesday();
            case WEDNESDAY:
                return isOnWednesday();
            case THURSDAY:
                return isOnThursday();
            case FRIDAY:
                return isOnFriday();
            case SATURDAY:
                return isOnSaturday();
            case SUNDAY:
                return isOnSunday();
        }

        System.out.println("toDo.scheduled.model.WeeklyScheduledTodo.matchesScheduledDayOfWeek - Unable to recognise day of the week!!");
        return false;
    }
}
