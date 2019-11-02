package toDo.scheduled.panels;


import javax.swing.*;
import java.awt.*;

public class WeeklySchedulePanel extends JPanel {

    private int width = 680, height = 180;
    private JCheckBox cbMonday = new JCheckBox("Monday");
    private JCheckBox cbTuesday = new JCheckBox("Tuesday");
    private JCheckBox cbWednesday = new JCheckBox("Wednesday");
    private JCheckBox cbThursday = new JCheckBox("Thursday");
    private JCheckBox cbFriday = new JCheckBox("Friday");
    private JCheckBox cbSaturday = new JCheckBox("Saturday");
    private JCheckBox cbSunday = new JCheckBox("Sunday");

    public WeeklySchedulePanel() {
        setPreferredSize(new Dimension(width, height));
        setLayout(new GridLayout(4, 2));

        add(cbMonday);
        add(cbTuesday);
        add(cbWednesday);
        add(cbThursday);
        add(cbFriday);
        add(cbSaturday);
        add(cbSunday);
    }

    public boolean isMondaySelected() {
        return cbMonday.isSelected();
    }
    public boolean isTuesdaySelected() {
        return cbTuesday.isSelected();
    }
    public boolean isWednesdaySelected() {
        return cbWednesday.isSelected();
    }
    public boolean isThursdaySelected() {
        return cbThursday.isSelected();
    }
    public boolean isFridaySelected() {
        return cbFriday.isSelected();
    }
    public boolean isSaturdaySelected() {
        return cbSaturday.isSelected();
    }
    public boolean isSundaySelected() {
        return cbSunday.isSelected();
    }

    public boolean isAtLeastOneDaySelected() {
        return isMondaySelected()
                || isTuesdaySelected()
                || isWednesdaySelected()
                || isThursdaySelected()
                || isFridaySelected()
                || isSaturdaySelected()
                || isSundaySelected();
    }
}
