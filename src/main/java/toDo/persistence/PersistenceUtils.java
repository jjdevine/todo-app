package toDo.persistence;

import toDo.common.ToDoPriority;

import java.util.Calendar;

public class PersistenceUtils {

    public static String marshallCalendar(Calendar cal) {
        if(cal == null) {
            return "null";
        }
        return cal.getTimeInMillis()+"";
    }

    public static Calendar unmarshallCalendar(String calStr) {
        if(calStr == null || calStr.equals("null")) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(calStr));
        return cal;
    }

    public static String marshallToDoPriority(ToDoPriority toDoPriority) {
        return toDoPriority.toString();
    }

    public static ToDoPriority unmarshallToDoPriority(String toDoPriorityStr) {
        return ToDoPriority.valueOf(toDoPriorityStr);
    }

    public static String marshallBoolean(boolean bool) {
        return Boolean.toString(bool);
    }

    public static boolean unmarshallBoolean(String boolStr) {
        return Boolean.parseBoolean(boolStr);
    }
}
