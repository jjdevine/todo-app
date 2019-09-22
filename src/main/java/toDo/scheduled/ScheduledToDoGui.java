package toDo.scheduled;

import toDo.exceptions.DateParseException;
import toDo.gui.customComponents.DateInput;
import toDo.scheduled.panels.WeeklySchedulePanel;
import toDo.scheduled.toDo.scheduled.models.RootJAXBScheduledToDo;
import toDo.scheduled.toDo.scheduled.models.ScheduledToDo;
import toDo.scheduled.toDo.scheduled.models.WeeklyScheduledTodo;
import toDo.utilities.GuiUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;

public class ScheduledToDoGui extends JFrame implements ActionListener {

    private int sWidth = 750, sHeight = 718;
    private int twoColWidth = sWidth/2 - 20;
    private JPanel panelHeader, panelScheduleType, panelEndDate, panelScheduleDetails, panelTitle, panelPriority, panelDescription, panelButtons;
    private JScrollPane jScrollPaneScheduleDetails;
    private WeeklySchedulePanel panelWeeklySchedule;
    private JPanel panelScheduleTypeLeft, panelScheduleTypeRight;
    private JPanel panelEndDateLeft, panelEndDateRight;
    private JLabel lHeader;
    private JTextField tfScheduleFrequency, tfTitle;
    private JTextArea taDescription;
    private JComboBox cbScheduleTypes, cbPriority;
    private DateInput endDateDateInput;
    private JButton bCreate, bCancel;

    public ScheduledToDoGui() {

        super("Create a Scheduled To Do");	//form heading
        //create container to place components in:
        Container container = getContentPane();
        container.setLayout(new FlowLayout());	//set flow layout

        //initialise
        panelHeader = GuiUtils.createBorderedPanel(sWidth - 20,50);
        panelScheduleType = GuiUtils.createBorderedPanel(sWidth - 20,50);
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
        cbScheduleTypes = GuiUtils.createComboBox((twoColWidth - 40)/2, 30, "Day(s)", "Week(s)", "Month(s)");
        panelScheduleTypeRight.add(tfScheduleFrequency);
        panelScheduleTypeRight.add(cbScheduleTypes);

        panelScheduleType.add(panelScheduleTypeLeft);
        panelScheduleType.add(panelScheduleTypeRight);

        /*
         * end date panel
         */

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

        cbPriority = GuiUtils.createComboBox(twoColWidth, 40, "Urgent", "High", "Medium", "Low");

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

        taDescription = GuiUtils.createJTextArea(twoColWidth-10, 80);
        panelDescriptionRight.add(taDescription);

        panelDescription.add(GuiUtils.createLabelPanel(twoColWidth, 90, 20,"With Description : "));
        panelDescription.add(panelDescriptionRight);

        /*
         * schedule details panel
         */

        panelWeeklySchedule = new WeeklySchedulePanel();

        jScrollPaneScheduleDetails = new JScrollPane(panelWeeklySchedule);
        jScrollPaneScheduleDetails.setPreferredSize(new Dimension(sWidth-30,190));
        jScrollPaneScheduleDetails.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        panelScheduleDetails.add(jScrollPaneScheduleDetails);


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

            if ("Weekly".equals(cbScheduleTypes.getSelectedItem())) {
                processWeekly();
            }
        }

    }

    private void processWeekly() {
        WeeklyScheduledTodo weeklyScheduledTodo = new WeeklyScheduledTodo();

        Calendar endDate;

        try
        {
            weeklyScheduledTodo.setEndDate(endDateDateInput.getDate());
        }
        catch(DateParseException ex)
        {
            JOptionPane.showMessageDialog(null, "Enter a valid date!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        EnumSet<WeeklyScheduledTodo.DaysOfWeek> selectedDays = panelWeeklySchedule.getSelectedDays();

        if(selectedDays.size() == 0) {
            GuiUtils.showError( "Select at least one day!");
            return;
        }

        weeklyScheduledTodo.setDaysOfWeek(selectedDays);

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(RootJAXBScheduledToDo.class);
            RootJAXBScheduledToDo wrapper = new RootJAXBScheduledToDo();
            wrapper.addScheduledToDos(weeklyScheduledTodo);

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            StringWriter output = new StringWriter();
            jaxbMarshaller.marshal(wrapper, output);

            System.out.println(output.toString());

        } catch (JAXBException e) {
            e.printStackTrace();
            GuiUtils.showError("JAXB binding failed!");
        }
    }

    public static void main(String [] args) {
        new ScheduledToDoGui();
    }


}
