package toDo.scheduled;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScheduledToDoGui extends JFrame implements ActionListener {

    private JPanel panelHeader, panelScheduleType, panelScheduleDetails, panelButtons;

    public ScheduledToDoGui() {

        super("Create a Scheduled To Do");	//form heading
        //create container to place components in:
        Container container = getContentPane();
        container.setLayout(new FlowLayout());	//set flow layout

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }

    public static void main(String [] args) {
        new ScheduledToDoGui();
    }
}
