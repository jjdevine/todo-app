package toDo.scheduled;

import toDo.scheduled.model.ScheduledTodo;
import toDo.utilities.GuiUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScheduledToDoViewGui extends JFrame implements ActionListener {

    private int sWidth = 820, sHeight = 808;
    private int panelWidth = sWidth - 25;

    private JPanel panelHeaders;
    private JPanel pFrequency, pStartDate, pEndDate, pPriority, pTitle, pDescription, pSchedule;
    private int col1X = 5;
    private int col2X = 105;
    private int col3X = 205;
    private int col4X = 305;
    private int col5X = 405;
    private int col6X = 505;
    private int col7X = 605;
    private int col8X = 705;

    public ScheduledToDoViewGui() {

        super("Current Scheduled To Dos");	//form heading
        //create container to place components in:
        Container container = getContentPane();
        container.setLayout(new FlowLayout());	//set flow layout

        int panelHeadersHeight = 50;
        panelHeaders = GuiUtils.createBorderedPanel(panelWidth, panelHeadersHeight);

        SpringLayout layoutPanelHeaders = new SpringLayout();
        panelHeaders.setLayout(layoutPanelHeaders);

        pFrequency = GuiUtils.createLabelPanel(100, 40, 12, "Frequency");
        pStartDate = GuiUtils.createLabelPanel(100, 40, 12, "Start Date");
        pEndDate = GuiUtils.createLabelPanel(100, 40, 12, "End Date");
        pPriority = GuiUtils.createLabelPanel(100, 40, 12, "Priority");
        pTitle = GuiUtils.createLabelPanel(100, 40, 12, "Title");
        pDescription = GuiUtils.createLabelPanel(100, 40, 12, "Description");
        pSchedule = GuiUtils.createLabelPanel(100, 40, 12, "Schedule");

        int yMargin = 5;
        GuiUtils.setNWDistanceFromParent(col1X, yMargin, pFrequency, panelHeaders, layoutPanelHeaders);
        GuiUtils.setNWDistanceFromParent(col2X, yMargin, pStartDate, panelHeaders, layoutPanelHeaders);
        GuiUtils.setNWDistanceFromParent(col3X, yMargin, pEndDate, panelHeaders, layoutPanelHeaders);
        GuiUtils.setNWDistanceFromParent(col4X, yMargin, pPriority, panelHeaders, layoutPanelHeaders);
        GuiUtils.setNWDistanceFromParent(col5X, yMargin, pTitle, panelHeaders, layoutPanelHeaders);
        GuiUtils.setNWDistanceFromParent(col6X, yMargin, pDescription, panelHeaders, layoutPanelHeaders);
        GuiUtils.setNWDistanceFromParent(col7X, yMargin, pSchedule, panelHeaders, layoutPanelHeaders);


        /*
            Panel ToDos
         */

        JScrollPane jspMain = GuiUtils.wrapWithScrollPane(sWidth-25, sHeight-panelHeadersHeight-50, createMainPanel());
        jspMain.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);


        add(panelHeaders);
        add(jspMain);

        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();	//get screen resoloution
        setLocation((d.width-sWidth)/2, (d.height-sHeight)/2);	//centre form
        setSize(sWidth,sHeight);	//set form size
        setVisible(true);//display screen

    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();

        SpringLayout layout = new SpringLayout();
        mainPanel.setLayout(layout);

        int xMargin = 5;
        int panelHeight = 50;

        for(int x=0; x< 30; x++) {
            ScheduledToDoDetailPanel itemPanel = new ScheduledToDoDetailPanel(null);
            int yOffset = ((panelHeight+5)*x)+5;
            GuiUtils.setNWDistanceFromParent(xMargin, yOffset, itemPanel, mainPanel, layout);
            System.out.println("x:" + xMargin + " ~ y: " + yOffset);
        }

        mainPanel.setPreferredSize(new Dimension(sWidth-25, 10*55));

        return mainPanel;
    }

    private void loadScheduledToDos() {

    }

    private class ScheduledToDoDetailPanel extends JPanel {

        private ScheduledTodo scheduledTodo;
        private int height = 50;

        public ScheduledToDoDetailPanel(ScheduledTodo scheduledTodo) {
            this.scheduledTodo = scheduledTodo;

            setBorder(new LineBorder(Color.BLACK));
            setPreferredSize(new Dimension(panelWidth-25, height));
        }
    }


    public static void main(String[] args) {
        new ScheduledToDoViewGui();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}
