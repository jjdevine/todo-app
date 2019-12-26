package toDo.scheduled.model;

import toDo.persistence.PersistenceManager;
import toDo.persistence.PersistenceModel;
import toDo.persistence.PersistenceUtils;
import toDo.utilities.ToDoUtilities;

import java.time.LocalDate;
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
        return incrementNextFireDate(LocalDate.now());
    }

    @Override
    public boolean incrementNextFireDateStartingTomorrow() {
        return incrementNextFireDate(LocalDate.now().plusDays(1));
    }

    private boolean incrementNextFireDate(LocalDate firstAvailableDate) {

        LocalDate date;

        //check next 3 years
        for(int offset=0; offset<1096; offset++) {
            date = firstAvailableDate.plusDays(offset);

            if(isDueToFireOnDate(date)) {
                nextFireDate = ToDoUtilities.getCalendarWithSameDate(getStartDate(), date);
                System.out.println("next fire date = " + ToDoUtilities.formatDate(nextFireDate));
                return true; //success
            }
        }

        return false; //no next increment - may have passed end date
    }

    public static void main(String[] args) throws Exception {
        LocalDate theDate = LocalDate.now().minusDays(50) ;

        MonthlyScheduledTodo monthlyTodo =
                new MonthlyScheduledTodo(
                        PersistenceManager.loadAllPersistenceModelsByType("ScheduledToDo").get(0));

        for(int offset =0; offset < 1200; offset++) {
            System.out.println("Date is " + theDate + " - isDueToFire? " + monthlyTodo.isDueToFireOnDate(theDate));
            theDate = theDate.plusDays(1);
        }

        System.out.println(monthlyTodo);

    }

    private boolean isDueToFireOnDate(LocalDate date) {

        LocalDate startDate = ToDoUtilities.convertCalendarToLocalDate(getStartDate());

        if(date.isAfter(ToDoUtilities.convertCalendarToLocalDate(getEndDate()))) {
            return false;
        }

        if (date.isBefore(startDate)) {
            return false;
        }

        if(!matchesScheduledDayOfMonth(date)) {
            return false;
        }

        if(!matchesScheduledMonth(date)) {
            return false;
        }

        return true;
    }

    private boolean matchesScheduledDayOfMonth(LocalDate date) {

        int dayOfMonth = date.getDayOfMonth();

        for(Integer scheduledDay: getScheduledDays()) {
            if(scheduledDay == dayOfMonth) {
                return true;
            }
        }

        return false;
     }

    private boolean matchesScheduledMonth(LocalDate date) {
        LocalDate startDate = ToDoUtilities.convertCalendarToLocalDate(getStartDate());
        LocalDate dateToCheck = startDate;

        while(!fallsInLaterMonth(dateToCheck, date)) {

            if(inSameMonth(dateToCheck, date)) {
                return true;
            }

            dateToCheck = dateToCheck.plusMonths(getFrequency());
        }

        return false;
    }

    private boolean inSameMonth(LocalDate date1, LocalDate date2) {
        if(date1.getYear() != date2.getYear()) {
            return false;
        }

        return date1.getMonthValue() == date2.getMonthValue();
    }

    private boolean fallsInLaterMonth(LocalDate date, LocalDate dateInTargetMonth) {
        if (date.getYear() > dateInTargetMonth.getYear()) {
            return true;
        }

        if (date.getYear() < dateInTargetMonth.getYear()) {
            return false;
        }

        return date.getMonthValue() > dateInTargetMonth.getMonthValue();

    }

}
