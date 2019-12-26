package toDo.scheduled;

import toDo.common.ToDoPriority;
import toDo.exceptions.DateParseException;
import toDo.gui.customComponents.DateInput;
import toDo.persistence.PersistenceManager;
import toDo.scheduled.model.MonthlyScheduledTodo;
import toDo.scheduled.model.ScheduledTodo;
import toDo.scheduled.model.WeeklyScheduledTodo;
import toDo.scheduled.panels.MonthlySchedulePanel;
import toDo.scheduled.panels.WeeklySchedulePanel;
import toDo.utilities.GuiUtils;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;


public class ScheduledToDoGui extends JFrame implements ActionListener {

    private int sWidth = 750, sHeight = 808;
    private int twoColWidth = sWidth/2 - 20;
    private JPanel panelHeader, panelScheduleType, panelStartDate, panelEndDate, panelScheduleDetails, panelTitle, panelPriority, panelDescription, panelButtons;
    private JScrollPane jScrollPaneScheduleDetails, jspDescription;
    private WeeklySchedulePanel panelWeeklySchedule;
    private MonthlySchedulePanel panelMonthlySchedule;
    private JPanel panelScheduleTypeLeft, panelScheduleTypeRight;
    private JPanel panelStartDateLeft, panelStartDateRight, panelEndDateLeft, panelEndDateRight;
    private JLabel lHeader;
    private JTextField tfScheduleFrequency, tfTitle;
    private JTextArea taDescription;
    private JComboBox cbScheduleTypes, cbPriority;
    private DateInput startDateDateInput, endDateDateInput;
    private JButton bCreate, bCancel;

    private final static String FREQUENCY_WEEKLY = "Week(s)";
    private final static String FREQUENCY_MONTHLY = "Month(s)";

    public ScheduledToDoGui() {

        super("Create a Scheduled To Do");	//form heading
        //create container to place components in:
        Container container = getContentPane();
        container.setLayout(new FlowLayout());	//set flow layout

        //initialise
        panelHeader = GuiUtils.createBorderedPanel(sWidth - 20,50);
        panelScheduleType = GuiUtils.createBorderedPanel(sWidth - 20,50);
        panelStartDate = GuiUtils.createBorderedPanel(sWidth - 20,85);
        panelEndDate = GuiUtils.createBorderedPanel(sWidth - 20,85);
        panelPriority = GuiUtils.createBorderedPanel(sWidth - 20,50);
        panelTitle =  GuiUtils.createBorderedPanel(sWidth - 20,50);
        panelDescription = GuiUtils.createBorderedPanel(sWidth - 20,100);
        panelScheduleDetails = GuiUtils.createBorderedPanel(sWidth - 20,200);
        panelButtons = GuiUtils.createBorderedPanel(sWidth - 20,50);

        /*
         * header panel
         */

        lHeader = new JLabel("Create a Scheduled To Do");
        lHeader.setFont(new Font("Comic Sans MS", Font.BOLD, 22));
        panelHeader.add(lHeader);

        /*
         * schedule type panel
         */

        panelScheduleTypeLeft = GuiUtils.createLabelPanel(twoColWidth, 40, 20, "Every :");
        panelScheduleTypeRight = GuiUtils.createRedBorderedPanel(twoColWidth, 40);

        tfScheduleFrequency = GuiUtils.createJTextField((twoColWidth - 40)/2, 30);
        cbScheduleTypes = GuiUtils.createComboBox((twoColWidth - 40)/2, 30, FREQUENCY_WEEKLY, FREQUENCY_MONTHLY);
        cbScheduleTypes.addActionListener(this);
        panelScheduleTypeRight.add(tfScheduleFrequency);
        panelScheduleTypeRight.add(cbScheduleTypes);

        panelScheduleType.add(panelScheduleTypeLeft);
        panelScheduleType.add(panelScheduleTypeRight);

        /*
         * start & end date panels
         */

        panelStartDateLeft = GuiUtils.createLabelPanel(twoColWidth, 75, 20, "Start Date :");
        panelStartDateRight = GuiUtils.createRedBorderedPanel(twoColWidth, 75);
        panelStartDate.add(panelStartDateLeft);
        panelStartDate.add(panelStartDateRight);

        startDateDateInput = new DateInput();
        startDateDateInput.insertTodaysDate();
        panelStartDateRight.add(startDateDateInput.getComponent());

        panelEndDateLeft = GuiUtils.createLabelPanel(twoColWidth, 75, 20, "End Date :");
        panelEndDateRight = GuiUtils.createRedBorderedPanel(twoColWidth, 75);
        panelEndDate.add(panelEndDateLeft);
        panelEndDate.add(panelEndDateRight);

        endDateDateInput = new DateInput();
        endDateDateInput.insertTodaysDate();
        panelEndDateRight.add(endDateDateInput.getComponent());

        /*
         * priority panel
         */

        cbPriority = GuiUtils.createComboBox(twoColWidth, 40, ToDoPriority.values());

        panelPriority.add(GuiUtils.createLabelPanel(twoColWidth, 40, 20,"With Priority : "));
        panelPriority.add(cbPriority);

        /*
         * Title Panel
         */

        tfTitle = GuiUtils.createJTextFieldWithMaxChars(twoColWidth-10, 30, 40);
        JPanel panelTitleRight = GuiUtils.createRedBorderedPanel(twoColWidth, 40);
        panelTitleRight.add(tfTitle);

        panelTitle.add(GuiUtils.createLabelPanel(twoColWidth, 40, 20,"With Title : "));
        panelTitle.add(panelTitleRight);

        /*
         * Description Panel
         */

        JPanel panelDescriptionRight = GuiUtils.createRedBorderedPanel(twoColWidth, 90);

        taDescription = GuiUtils.createJTextArea();
        jspDescription = GuiUtils.wrapWithScrollPane(twoColWidth-10, 80, taDescription);
        panelDescriptionRight.add(jspDescription);

        panelDescription.add(GuiUtils.createLabelPanel(twoColWidth, 90, 20,"With Description : "));
        panelDescription.add(panelDescriptionRight);

        /*
         * schedule details panel
         */

        panelWeeklySchedule = new WeeklySchedulePanel();
        panelMonthlySchedule = new MonthlySchedulePanel();

        jScrollPaneScheduleDetails = new JScrollPane();
        jScrollPaneScheduleDetails.setPreferredSize(new Dimension(sWidth-30,190));
        jScrollPaneScheduleDetails.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panelScheduleDetails.add(jScrollPaneScheduleDetails);
        jScrollPaneScheduleDetails.setViewportView(panelWeeklySchedule);  //default to weekly view

        /*
         * button panel
         */

        int buttonWidth = sWidth/2 - 40;
        int buttonHeight = 40;

        bCreate = GuiUtils.createButtonWithListener(buttonWidth, buttonHeight, "Create", this);
        bCancel = GuiUtils.createButtonWithListener(buttonWidth, buttonHeight, "Cancel", this);

        panelButtons.add(bCreate);
        panelButtons.add(bCancel);

        /*
         * add panels to container
         */

        container.add(panelHeader);
        container.add(panelScheduleType);
        container.add(panelStartDate);
        container.add(panelEndDate);
        container.add(panelPriority);
        container.add(panelTitle);
        container.add(panelDescription);
        container.add(panelScheduleDetails);
        container.add(panelButtons);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();	//get screen resoloution
        setLocation((d.width-sWidth)/2, (d.height-sHeight)/2);	//centre form

        setSize(sWidth,sHeight);	//set form size
        setVisible(true);//display screen

    }

    @Override
    public void actionPerformed(ActionEvent event) {

        if(event.getSource() == bCreate) {

            ScheduledTodo scheduledTodo = null;
            Function<ScheduledTodo, Boolean> frequencyStrategy = null;

            if (FREQUENCY_MONTHLY.equals(cbScheduleTypes.getSelectedItem())) {
                frequencyStrategy = new Function<ScheduledTodo, Boolean>() {
                    @Override
                    public Boolean apply(ScheduledTodo scheduledTodo) {
                        MonthlyScheduledTodo monthlyScheduledTodo = (MonthlyScheduledTodo) scheduledTodo;
                        return populateMonthlyScheduledTodo(monthlyScheduledTodo);
                    }
                };

                scheduledTodo = new MonthlyScheduledTodo();

            } else if (FREQUENCY_WEEKLY.equals(cbScheduleTypes.getSelectedItem())) {
                frequencyStrategy = new Function<ScheduledTodo, Boolean>() {
                    @Override
                    public Boolean apply(ScheduledTodo scheduledTodo) {
                        WeeklyScheduledTodo weeklyScheduledTodo = (WeeklyScheduledTodo) scheduledTodo;
                        return populateWeeklyScheduledTodo(weeklyScheduledTodo);
                    }
                };

                scheduledTodo = new WeeklyScheduledTodo();

            } else {
                GuiUtils.showError("No Frequency Selected!");
                return;
            }

            if(!populateScheduledTodo(scheduledTodo)) {
                return; //validation error
            }

            if(!frequencyStrategy.apply(scheduledTodo)) {
                return; //validation error
            }

            if(!scheduledTodo.incrementNextFireDateIncludingToday()) {
                GuiUtils.showError("No fire date detected in next 3 years, try again");
                return;
            }

            try {
                PersistenceManager.persist(scheduledTodo.toPersistenceModel());
                GuiUtils.showInformation("Scheduled ToDo Created!", "Success");
                dispose();
            } catch (IOException ex) {
                GuiUtils.showError("Could not created scheduled To Do: " + ex.getMessage());
                ex.printStackTrace();
                return;
            }

        } else if(event.getSource() == cbScheduleTypes) {

            switch ((String)cbScheduleTypes.getSelectedItem()) {
                case FREQUENCY_WEEKLY:
                    jScrollPaneScheduleDetails.setViewportView(panelWeeklySchedule);
                    break;
                case FREQUENCY_MONTHLY:
                    jScrollPaneScheduleDetails.setViewportView(panelMonthlySchedule);
                    break;
            }
        } else if(event.getSource() == bCancel) {
            dispose();
        }
    }

    /**
     *
     * @param scheduledTodo
     * @return true on success, false on error
     */
    private boolean populateScheduledTodo(ScheduledTodo scheduledTodo) {

        //frequency
        int frequency = -1;

        try {
            frequency = Integer.parseInt(tfScheduleFrequency.getText());
            if(frequency < 1) {
                throw new NumberFormatException("Number is too low");
            }
        } catch (NumberFormatException nfe) {
            GuiUtils.showError("Frequency must be a number greater than 0");
            return false;
        }

        //start date
        Calendar startDate = null;

        try {
            startDate = startDateDateInput.getDate();
        } catch (DateParseException e) {
            GuiUtils.showError("Start Date is not valid");
            return false;
        }

        if (startDate == null) {
            GuiUtils.showError("Start Date is not valid");
            return false;
        }

        //end date
        Calendar endDate = null;

        try {
            endDate = endDateDateInput.getDate();
        } catch (DateParseException e) {
            GuiUtils.showError("End Date is not valid");
            return false;
        }

        if (endDate == null) {
            GuiUtils.showError("End Date is not valid");
            return false;
        }

        if(!endDate.after(startDate)) {
            GuiUtils.showError("Start Date must be before End Date");
            return false;
        }

        //priority
        ToDoPriority priority = (ToDoPriority) cbPriority.getSelectedItem();

        //Title
        String title = tfTitle.getText().trim();

        if(title.length() == 0) {
            GuiUtils.showError("Title must not be blank");
            return false;
        }

        //description
        String description = taDescription.getText().trim();

        if(description.length() == 0) {
            GuiUtils.showError("Description must not be blank");
            return false;
        }

        scheduledTodo.setFrequency(frequency);
        scheduledTodo.setStartDate(startDate);
        scheduledTodo.setEndDate(endDate);
        scheduledTodo.setToDoPriority(priority);
        scheduledTodo.setTitle(title);
        scheduledTodo.setDescription(description);

        return true;
    }

    /**
     *
     * @param monthlyScheduledTodo
     * @return true if completes successfully, false on validation error
     */
    private boolean populateMonthlyScheduledTodo(MonthlyScheduledTodo monthlyScheduledTodo) {

        Set<Integer> selectedDays = new HashSet<>();
        selectedDays.addAll(panelMonthlySchedule.getSelectedDays());

        if(selectedDays.size() == 0) {
            GuiUtils.showError("Select at least one day on which to generate this To Do");
            return false;
        }

        monthlyScheduledTodo.setScheduledDays(selectedDays);
        return true;
    }

    /**
     *
     * @param weeklyScheduledTodo
     * @return true if completes successfully, false on validation error
     */
    private boolean populateWeeklyScheduledTodo(WeeklyScheduledTodo weeklyScheduledTodo) {

        if(!panelWeeklySchedule.isAtLeastOneDaySelected()) {
            GuiUtils.showError("Must select at least one day of the week!");
            return false;
        }

        weeklyScheduledTodo.setOnMonday(panelWeeklySchedule.isMondaySelected());
        weeklyScheduledTodo.setOnTuesday(panelWeeklySchedule.isTuesdaySelected());
        weeklyScheduledTodo.setOnWednesday(panelWeeklySchedule.isWednesdaySelected());
        weeklyScheduledTodo.setOnThursday(panelWeeklySchedule.isThursdaySelected());
        weeklyScheduledTodo.setOnFriday(panelWeeklySchedule.isFridaySelected());
        weeklyScheduledTodo.setOnSaturday(panelWeeklySchedule.isSaturdaySelected());
        weeklyScheduledTodo.setOnSunday(panelWeeklySchedule.isSundaySelected());

        return true;
    }


    public static void main(String [] args) {
        new ScheduledToDoGui();
    }


}
