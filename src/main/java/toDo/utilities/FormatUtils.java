package toDo.utilities;

import java.text.DateFormat;
import java.util.Calendar;

public class FormatUtils {

    public static String CalendarToShortDescription(Calendar cal) {
        return DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(cal.getTime());
    }

    public static String truncateString(String str, int maxLen) {
        str = str.replaceAll("\n", " - ");
        if(str.length() > maxLen) {
            str = str.substring(0,maxLen-3) + "...";
        }

        return str;
    }
}
