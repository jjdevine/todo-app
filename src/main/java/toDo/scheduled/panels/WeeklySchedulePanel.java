package toDo.scheduled.panels;

import toDo.scheduled.toDo.scheduled.models.WeeklyScheduledTodo;
import toDo.utilities.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.util.EnumSet;

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

    public EnumSet<WeeklyScheduledTodo.DaysOfWeek> getSelectedDays() {

        EnumSet<WeeklyScheduledTodo.DaysOfWeek> selectedDays = EnumSet.noneOf(WeeklyScheduledTodo.DaysOfWeek.class);


        if (cbMonday.isSelected()) {
            selectedDays.add(WeeklyScheduledTodo.DaysOfWeek.MONDAY);
        } else if (cbTuesday.isSelected()) {
            selectedDays.add(WeeklyScheduledTodo.DaysOfWeek.TUESDAY);
        } else if (cbWednesday.isSelected()) {
            selectedDays.add(WeeklyScheduledTodo.DaysOfWeek.WEDNESDAY);
        } else if (cbThursday.isSelected()) {
            selectedDays.add(WeeklyScheduledTodo.DaysOfWeek.THURSDAY);
        } else if (cbFriday.isSelected()) {
            selectedDays.add(WeeklyScheduledTodo.DaysOfWeek.FRIDAY);
        } else if (cbSaturday.isSelected()) {
            selectedDays.add(WeeklyScheduledTodo.DaysOfWeek.SATURDAY);
        } else if (cbSunday.isSelected()) {
            selectedDays.add(WeeklyScheduledTodo.DaysOfWeek.SUNDAY);
        }

        return selectedDays;
    }

}
