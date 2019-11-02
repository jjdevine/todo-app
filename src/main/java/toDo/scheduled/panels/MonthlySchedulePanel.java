package toDo.scheduled.panels;

import toDo.utilities.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MonthlySchedulePanel extends JPanel implements ActionListener{

    private int width = 680, height = 180;
    private JButton bAdd, bRemove, bRemoveAll;
    private JLabel lDay;
    private JTextField tfDay;
    private JList<String> listDays;
    private JScrollPane jspListDays;
    private JPanel panelButtons, panelList;
    private List<Integer> selectedDays = new ArrayList<>();

    public MonthlySchedulePanel() {

        setLayout(new FlowLayout());

        int panelX = (width/2)+ 10;
        int panelY = height - 5;

        panelButtons = GuiUtils.createBorderedPanel(panelX, panelY);
        panelList = GuiUtils.createBorderedPanel(panelX, panelY);

        /*
            panel Buttons
         */

        lDay = GuiUtils.createJLabel(panelX/2, 30, "Day of Month:", 16);
        tfDay = GuiUtils.createJTextFieldWithMaxChars(30, 30, 2);

        int buttonX = panelX-5;
        int buttonY = 40;

        bAdd = GuiUtils.createButtonWithListener(buttonX, buttonY, "Add", this);
        bRemove = GuiUtils.createButtonWithListener(buttonX, buttonY, "Remove", this);
        bRemoveAll = GuiUtils.createButtonWithListener(buttonX, buttonY, "Clear List", this);

        panelButtons.add(lDay);
        panelButtons.add(tfDay);
        panelButtons.add(bAdd);
        panelButtons.add(bRemove);
        panelButtons.add(bRemoveAll);

        /*
            panel List
         */

        DefaultListModel<String> dlm = new DefaultListModel<>();

        listDays = new JList<>(dlm);
        jspListDays = new JScrollPane(listDays);

        jspListDays.setPreferredSize(new Dimension(panelX-10, panelY-10));
        panelList.add(jspListDays);

        /*
        add panels to parent
         */

        add(panelButtons);
        add(panelList);

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

        if(actionEvent.getSource() == bAdd) {
            processAdd();
        } else if(actionEvent.getSource() == bRemove) {
            processRemove();
        } else if(actionEvent.getSource() == bRemoveAll) {
            processRemoveAll();
        }
    }

    private void processAdd() {

        int inputDate = -1;

        try {
            inputDate = Integer.parseInt(tfDay.getText());
        } catch (NumberFormatException nfe) {
            GuiUtils.showError(tfDay.getText() + " is not a valid day of the month!");
            return;
        }

        if(inputDate < 1 || inputDate > 31) {
            GuiUtils.showError("Day of month must be between 1 and 31");
            tfDay.setText("");
            tfDay.requestFocus();
            return;
        }

        if(selectedDays.contains(inputDate)) {
            //duplicate - do nothing
            tfDay.setText("");
            tfDay.requestFocus();
            return;
        }

        selectedDays.add(inputDate);
        selectedDays.sort((a,b) -> a-b);

        tfDay.setText("");
        tfDay.requestFocus();

        refreshSelectedDaysModel();
    }

    private void refreshSelectedDaysModel() {

        DefaultListModel dlm = new DefaultListModel();

        selectedDays.forEach(day -> dlm.addElement(day));

        listDays.setModel(dlm);
    }

    private void processRemove() {

        int index = listDays.getSelectedIndex();

        if(index == -1) {
            GuiUtils.showError("Select a date to remove");
            return;
        }

        selectedDays.remove(index);
        refreshSelectedDaysModel();
    }

    private void processRemoveAll() {
        selectedDays.clear();
        refreshSelectedDaysModel();
    }

    public List<Integer> getSelectedDays() {
        return Collections.unmodifiableList(selectedDays);
    }
}