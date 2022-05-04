package toDo.gui.customComponents;

import toDo.utilities.Global;
import toDo.utilities.GuiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AlertPane extends JScrollPane implements ActionListener {

    public enum AlertType {INFORMATION, WARNING}
    private int width, height;
    private List<JPanel> alertPanels = new ArrayList<>();

    private JPanel masterPanel = GuiUtils.createBorderedPanel();

    public AlertPane(int width) {
        masterPanel = GuiUtils.createBorderedPanel();
        masterPanel.setLayout(new FlowLayout());

        setViewportView(masterPanel);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        this.width = width;
        reRender();
    }

    private void setHeight(int height) {
        this.height = height;
        setPreferredSize(new Dimension(width, height));
    }

    public int getHeight() {
        return height;
    }

    public synchronized void addAlert(AlertType type, String message) {
        createAlertMessage(type, message);
    }

    private void createAlertMessage(AlertType type, String message) {
        JPanel alertPanel = GuiUtils.createBorderedPanel(width-20, 40);

        JLabel label = GuiUtils.createJLabel(width-150, 30, message, 12);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setToolTipText(message);

        if(type == AlertType.WARNING) {
            label.setForeground(Color.RED);
        }

        alertPanel.add(label);
        alertPanel.add(GuiUtils.createButtonWithListener(100, 30, "Dismiss", this));
        alertPanels.add(alertPanel);
        masterPanel.add(alertPanel);
        reRender();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton button = (JButton)e.getSource();
        JPanel parent = (JPanel)button.getParent();
        alertPanels.remove(parent);
        masterPanel.remove(parent);
        reRender();
    }

    private void reRender() {

        if(alertPanels.size() == 0) {
            setHeight(0);
            setVisible(false);
        } else if(alertPanels.size() > 5) {
            setHeight(10 + (45*5));
            setVisible(true);
            setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        } else {
            setHeight(10 + (45*alertPanels.size()));
            setVisible(true);
            setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        }

        masterPanel.setPreferredSize(new Dimension(width-10, 5 + (45*alertPanels.size())));
        masterPanel.revalidate();
        revalidate();
        repaint();
        Global.refreshView();

        //Not supported but would be nice to show number of messages..
        //System.out.println(Taskbar.getTaskbar().isSupported(Taskbar.Feature.ICON_BADGE_TEXT));
    }

}
