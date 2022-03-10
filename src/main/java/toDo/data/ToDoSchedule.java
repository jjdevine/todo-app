package toDo.data;

import toDo.utilities.ToDoUtilities;

import java.util.Calendar;
import java.util.Map;

public class ToDoSchedule {

    public enum TargetPriority {NEXT_PRIORITY, URGENT, HIGH, MEDIUM}

    private Calendar startDate;
    private Calendar nextDueDate;
    private int dayFrequency;
    private TargetPriority targetPriority;
    private boolean notify;

    public ToDoSchedule(Calendar startDate, int dayFrequency, TargetPriority targetPriority, boolean notify) {
        this.startDate = startDate;
        this.dayFrequency = dayFrequency;
        this.targetPriority = targetPriority;
        this.notify = notify;
        rollToNextDueDate();
    }

    private ToDoSchedule(Calendar startDate, Calendar nextDueDate, int dayFrequency, TargetPriority targetPriority, boolean notify) {
        this.startDate = startDate;
        this.dayFrequency = dayFrequency;
        this.targetPriority = targetPriority;
        this.notify = notify;
        this.nextDueDate = nextDueDate;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public int getDayFrequency() {
        return dayFrequency;
    }

    public TargetPriority getTargetPriority() {
        return targetPriority;
    }

    public Calendar getNextDueDate() {
        return nextDueDate;
    }

    public boolean isNotify() {
        return notify;
    }

    public void rollToNextDueDate() {
        //if not yet initialised will always be start date + day frequency
        if(nextDueDate == null) {
            nextDueDate = Calendar.getInstance();
            nextDueDate.setTime(startDate.getTime());
            nextDueDate.set(Calendar.HOUR_OF_DAY, 0);
            nextDueDate.set(Calendar.MINUTE, 0);
            nextDueDate.set(Calendar.SECOND, 0);
            nextDueDate.add(Calendar.DATE, dayFrequency);
            return;
        }

        //roll to next future date in schedule
        while(nextDueDate.before(Calendar.getInstance())) {
            nextDueDate.add(Calendar.DATE, dayFrequency);
            nextDueDate.set(Calendar.HOUR_OF_DAY, 0);
            nextDueDate.set(Calendar.MINUTE, 0);
            nextDueDate.set(Calendar.SECOND, 0);
        }
    }

    public String toXml() {
        StringBuilder result = new StringBuilder();
        result.append("\t<schedule>\n\t\t");
        result.append(ToDoUtilities.asXMLElement(
                "startDate",
                ToDoUtilities.formatDateToDD_MM_YYYY_hh_mm(startDate)));
        result.append("\n\t\t");
        result.append(ToDoUtilities.asXMLElement(
                "nextDueDate",
                ToDoUtilities.formatDateToDD_MM_YYYY_hh_mm(nextDueDate)));
        result.append("\n\t\t");
        result.append(ToDoUtilities.asXMLElement(
                "dayFrequency",
                dayFrequency + ""));
        result.append("\n\t\t");
        result.append(ToDoUtilities.asXMLElement(
                "targetPriority",
                targetPriority.name()));
        result.append("\n\t\t");
        result.append(ToDoUtilities.asXMLElement(
                "notify",
                Boolean.toString(notify)));
        result.append("\n\t");
        result.append("</schedule>");

        return result.toString();
    }

    public static ToDoSchedule buildFromXML(String xml) {
        System.out.println("Building To Do Schedule from...");
        System.out.println(xml);

        Calendar startDate = null;
        Calendar nextDueDate = null;
        int dayFrequency = -1;
        TargetPriority targetPriority = null;
        boolean notify = false;

        Map<String, String> attributes = ToDoUtilities.extractXmlAttributes(xml); //TODO: do something with this

        for(String key: attributes.keySet()) {
            String value = attributes.get(key);

            if(key.equalsIgnoreCase("nextDueDate")) {
                nextDueDate = ToDoUtilities.convertStringDateTimeToCalendar(value);
            } else if(key.equalsIgnoreCase("startDate")) {
                startDate = ToDoUtilities.convertStringDateTimeToCalendar(value);
            } else if(key.equalsIgnoreCase("dayFrequency")) {
                dayFrequency = Integer.parseInt(value);
            } else if(key.equalsIgnoreCase("targetPriority")) {
                targetPriority = TargetPriority.valueOf(value);
            } else if(key.equalsIgnoreCase("notify")) {
                notify = Boolean.parseBoolean(value);
            }
        }

        return new ToDoSchedule(startDate, nextDueDate, dayFrequency, targetPriority, notify);
    }

    @Override
    public String toString() {
        return "ToDoSchedule{" +
                "startDate=" + startDate.getTime() +
                ", nextDueDate=" + nextDueDate.getTime() +
                ", dayFrequency=" + dayFrequency +
                ", targetPriority=" + targetPriority +
                '}';
    }
}
