package toDo.scheduled;

import toDo.persistence.PersistenceManager;
import toDo.scheduled.model.ScheduledTodo;
import toDo.utilities.FormatUtils;
import toDo.utilities.GuiUtils;
import toDo.utilities.ToDoUtilities;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ScheduledToDoViewGui extends JFrame implements ActionListener {

    private int sWidth = 1350, sHeight = 808;
    private int panelWidth = sWidth - 25;

    private JPanel panelHeaders;
    private JPanel pFrequency, pStartDate, pEndDate, pPriority, pTitle, pDescription, pSchedule, pNextFireDate;
    private JScrollPane jspMain;
    private int col1X = 5;
    private int col2X = 125;
    private int col3X = 245;
    private int col4X = 365;
    private int col5X = 465;
    private int col6X = 605;
    private int col7X = 905;
    private int col8X = 1050;
    private int col9X = 1200;
    private int colWidth = 145;

    private List<ScheduledTodo> scheduledTodos = new ArrayList<>();

    public ScheduledToDoViewGui() {

        //create container to place components in:
        Container container = getContentPane();
        container.setLayout(new FlowLayout());	//set flow layout

        int panelHeadersHeight = 50;
        panelHeaders = GuiUtils.createBorderedPanel(panelWidth, panelHeadersHeight);

        SpringLayout layoutPanelHeaders = new SpringLayout();
        panelHeaders.setLayout(layoutPanelHeaders);

        int margin = 5;
        pFrequency = GuiUtils.createLabelPanel(col2X-col1X-margin, 40, 12, "Frequency");
        pStartDate = GuiUtils.createLabelPanel(col3X-col2X-margin, 40, 12, "Start Date");
        pEndDate = GuiUtils.createLabelPanel(col4X-col3X-margin, 40, 12, "End Date");
        pPriority = GuiUtils.createLabelPanel(col5X-col4X-margin, 40, 12, "Priority");
        pTitle = GuiUtils.createLabelPanel(col6X-col5X-margin, 40, 12, "Title");
        pDescription = GuiUtils.createLabelPanel(col7X-col6X-margin, 40, 12, "Description");
        pSchedule = GuiUtils.createLabelPanel(col8X-col7X-margin, 40, 12, "Schedule");
        pNextFireDate = GuiUtils.createLabelPanel(col9X-col8X-margin, 40, 12, "Next Fire Date");

        int yMargin = 5;
        int offset = 5;
        GuiUtils.setNWDistanceFromParent(offset+col1X, yMargin, pFrequency, panelHeaders, layoutPanelHeaders);
        GuiUtils.setNWDistanceFromParent(offset+col2X, yMargin, pStartDate, panelHeaders, layoutPanelHeaders);
        GuiUtils.setNWDistanceFromParent(offset+col3X, yMargin, pEndDate, panelHeaders, layoutPanelHeaders);
        GuiUtils.setNWDistanceFromParent(offset+col4X, yMargin, pPriority, panelHeaders, layoutPanelHeaders);
        GuiUtils.setNWDistanceFromParent(offset+col5X, yMargin, pTitle, panelHeaders, layoutPanelHeaders);
        GuiUtils.setNWDistanceFromParent(offset+col6X, yMargin, pDescription, panelHeaders, layoutPanelHeaders);
        GuiUtils.setNWDistanceFromParent(offset+col7X, yMargin, pSchedule, panelHeaders, layoutPanelHeaders);
        GuiUtils.setNWDistanceFromParent(offset+col8X, yMargin, pNextFireDate, panelHeaders, layoutPanelHeaders);


        /*
            Panel ToDos
         */

        jspMain = GuiUtils.wrapWithScrollPane(sWidth-25, sHeight-panelHeadersHeight-50, createMainPanel());
        jspMain.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        add(panelHeaders);
        add(jspMain);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();	//get screen resoloution
        setLocation((d.width-sWidth)/2, (d.height-sHeight)/2);	//centre form
        setSize(sWidth,sHeight);	//set form size
        setVisible(true);//display screen

    }

    private void updateTitle() {
        setTitle("Scheduled To Dos - " + scheduledTodos.size() + " currently scheduled to dos");
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();

        SpringLayout layout = new SpringLayout();
        mainPanel.setLayout(layout);

        int xMargin = 5;
        int panelHeight = 70;

        scheduledTodos = loadScheduledToDos();

        updateTitle();

        int index = 0;
        for(ScheduledTodo scheduledTodo: scheduledTodos) {
            ScheduledToDoDetailPanel itemPanel = new ScheduledToDoDetailPanel(scheduledTodo, this);
            int yOffset = ((panelHeight+5)*index)+5;
            GuiUtils.setNWDistanceFromParent(xMargin, yOffset, itemPanel, mainPanel, layout);
            index++;
        }

        mainPanel.setPreferredSize(new Dimension(sWidth-25, 5+(scheduledTodos.size()*(panelHeight+5))));

        return mainPanel;
    }

    private List<ScheduledTodo> loadScheduledToDos() {
        try {
            return ScheduledToDoService.loadAllScheduledToDos();
        } catch (IOException ex ) {
            ex.printStackTrace();
            GuiUtils.showError("Unable to load scheduled Todos");
            return Collections.emptyList();
        }
    }

    private class ScheduledToDoDetailPanel extends JPanel {

        private ScheduledTodo scheduledTodo;
        private int height = 70;

        public ScheduledToDoDetailPanel(ScheduledTodo scheduledTodo, ActionListener listener) {
            this.scheduledTodo = scheduledTodo;

            setBorder(new LineBorder(Color.BLACK));
            setPreferredSize(new Dimension(panelWidth-25, height));

            SpringLayout layout = new SpringLayout();
            setLayout(layout);

            int xMargin = 5;
            int yMargin = 5;
            int elementHeight = 60;
            GuiUtils.setNWDistanceFromParent(col1X, yMargin,
                    GuiUtils.createReadOnlyTextField(col2X - col1X - xMargin, elementHeight, scheduledTodo.getFrequencyDescription()),
                    this, layout);

            GuiUtils.setNWDistanceFromParent(col2X, yMargin,
                    GuiUtils.createReadOnlyTextField(col3X - col2X - xMargin, elementHeight, FormatUtils.CalendarToShortDescription(scheduledTodo.getStartDate())),
                    this, layout);

            GuiUtils.setNWDistanceFromParent(col3X, yMargin,
                    GuiUtils.createReadOnlyTextField(col4X - col3X - xMargin, elementHeight, FormatUtils.CalendarToShortDescription(scheduledTodo.getEndDate())),
                    this, layout);

            GuiUtils.setNWDistanceFromParent(col4X, yMargin,
                    GuiUtils.createReadOnlyTextField(col5X - col4X - xMargin, elementHeight, scheduledTodo.getToDoPriority().toString()),
                    this, layout);

            GuiUtils.setNWDistanceFromParent(col5X, yMargin,
                    GuiUtils.createReadOnlyTextField(col6X - col5X - xMargin, elementHeight, FormatUtils.truncateString(scheduledTodo.getTitle(), 20)),
                    this, layout);

            GuiUtils.setNWDistanceFromParent(col6X, yMargin,
                    GuiUtils.createReadOnlyScrollingTextArea(col7X - col6X - xMargin, elementHeight, scheduledTodo.getDescription()),
                    this, layout);

            GuiUtils.setNWDistanceFromParent(col7X, yMargin,
                    GuiUtils.createReadOnlyScrollingTextArea(col8X - col7X - xMargin, elementHeight, scheduledTodo.getScheduleDescription()),
                    this, layout);

            GuiUtils.setNWDistanceFromParent(col8X, yMargin,
                    GuiUtils.createReadOnlyScrollingTextArea(col9X - col8X - xMargin, elementHeight, ToDoUtilities.formatCalendar(scheduledTodo.getNextFireDate())),
                    this, layout);

            JButton bDelete = GuiUtils.createButtonWithListener(panelWidth - 30 - col9X - xMargin, elementHeight, "Delete", listener);
            bDelete.setActionCommand(scheduledTodo.getId());

            GuiUtils.setNWDistanceFromParent(col9X, yMargin, bDelete,this, layout);
        }
    }


    public static void main(String[] args) {
        new ScheduledToDoViewGui();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        //action command will be id of scheduled to do to delete
        String id = actionEvent.getActionCommand();

        for(ScheduledTodo scheduledTodo: scheduledTodos) {
            if(scheduledTodo.getId().equals(id)) {
                try {
                    PersistenceManager.deletePersistenceModel(scheduledTodo.toPersistenceModel());
                    jspMain.setViewportView(createMainPanel());
                    GuiUtils.showInformation("Scheduled To Do Deleted!", "Success");
                } catch (IOException ex) {
                    ex.printStackTrace();
                    GuiUtils.showError("Unable to delete persistence model: " + ex.getMessage());
                }
            }
        }
    }
}
