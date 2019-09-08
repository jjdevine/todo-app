package toDo.gui.alert;
import toDo.alert.Alert;
import toDo.data.ToDoItem;
import toDo.gui.customComponents.AlertHolder;
import toDo.interfaces.*;
import toDo.utilities.ToDoUtilities;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class AlertViewer extends JFrame implements ActionListener {

	private static final long serialVersionUID = -6642825890624044593L;
	private int sWidth = 740, sHeight = 600;
	public static final int JSP_HEIGHT = 520;
	private JPanel panelHeader, panelMain;
	private List<Alert> listAlerts;
	private List<AlertHolder> listAlertHolders = new ArrayList<AlertHolder>();
	private JLabel lDate, lType, lNotify, lToDo;
	private Alertable alertable;
	private List<ToDoItem> listToDos;
	private JScrollPane jsp = new JScrollPane();
	
	public AlertViewer(Alertable alertable, List<ToDoItem> listToDos)
	{
		super("Active Alerts");	//form heading
		//create container to place components in:
		Container container = getContentPane();
		container.setLayout(new FlowLayout());	//set flow layout
		
		/*
		 * save references
		 */
		
		this.alertable = alertable;
		this.listToDos = listToDos;
		listAlerts = alertable.getListAlerts();
		
		/*
		 * create panels
		 */
		
		panelHeader = new JPanel();
		panelMain = new JPanel();
		
		panelHeader.setPreferredSize(new Dimension(sWidth-20, 25));
		
		panelHeader.setBorder(new LineBorder(Color.BLACK));
		
		/*
		 * panelHeader
		 */
		
		lDate = new JLabel("Alert Time", JLabel.CENTER);
		lType = new JLabel("Alert Type", JLabel.CENTER);
		lNotify = new JLabel("Notify", JLabel.CENTER);
		lToDo = new JLabel("To Do Affected", JLabel.CENTER);
		
		lDate.setPreferredSize(new Dimension(90,15));
		lType.setPreferredSize(new Dimension(75,15));
		lNotify.setPreferredSize(new Dimension(40,15));
		lToDo.setPreferredSize(new Dimension(300,15));
		
		panelHeader.add(lDate);
		panelHeader.add(lType);
		panelHeader.add(lNotify);
		panelHeader.add(lToDo);
		
		JLabel lDummy = new JLabel(" ");
		lDummy.setPreferredSize(new Dimension(170,15));
		
		panelHeader.add(lDummy);
		panelHeader.add(lDummy);
		
		/*
		 * panel main
		 */
		
		panelMain = createMainPanel();
		jsp = new JScrollPane(panelMain, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(sWidth-20, JSP_HEIGHT));
		
		/*
		 * add to container
		 */
		
		container.add(panelHeader);
		container.add(jsp);
		
		/*
		 * rendering
		 */
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();	//get screen resoloution
		setLocation((d.width-sWidth)/2, (d.height-sHeight)/2);	//centre form
		
		setSize(sWidth,sHeight);	//set form size
		setVisible(true);//display screen
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		for(AlertHolder holder: listAlertHolders)
		{
			if(e.getSource() == holder.getBDetails())
			{
				Alert a = holder.getAlert();
				new AlertDetailsView(a, ToDoUtilities.getToDoByID(a.getToDoID(), listToDos).getDescription());
			}
			else if(e.getSource() == holder.getBDelete())
			{
				int optionPicked = JOptionPane.showConfirmDialog(this, "Really delete alert on '" + ToDoUtilities.getToDoByID(holder.getAlert().getToDoID(), listToDos).getDescription() + "'?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
				
				if (optionPicked == JOptionPane.YES_OPTION)
				{
					alertable.removeAlert(holder.getAlert());
					refreshDisplay();
				}
			}
		}

	}

	public List<Alert> getListAlerts() {
		return listAlerts;
	}

	public void setListAlerts(List<Alert> listAlerts) {
		this.listAlerts = listAlerts;
	}

	public List<ToDoItem> getListToDos() {
		return listToDos;
	}

	public void setListToDos(List<ToDoItem> listToDos) {
		this.listToDos = listToDos;
	}
	
	/**
	 * builds a new panel showing a list of alerts
	 * @return
	 */
	public JPanel createMainPanel()
	{
		/*
		 * create panel
		 */
		
		JPanel newPanelMain = new JPanel();
		
		SpringLayout sLayout = new SpringLayout();
		
		newPanelMain.setBorder(new LineBorder(Color.BLACK));
		newPanelMain.setLayout(sLayout);
		
		/*
		 * sort list and wrap in holders
		 */
		
		Collections.sort(listAlerts);
		
		listAlertHolders = ToDoUtilities.wrapAlerts(listAlerts, listToDos);
		
		/*
		 * add holders to panel
		 */
		
		boolean firstIteration = true;
		JPanel previousRowPanel = null;
		int panelHeight = 0;
		
		for(AlertHolder holder: listAlertHolders)
		{
			JPanel newRowPanel = holder.getJPanel();
			
			if(firstIteration)
			{
				firstIteration = false;
				sLayout.putConstraint(SpringLayout.NORTH, newPanelMain, 3, SpringLayout.NORTH, newRowPanel);
				
				panelHeight += 38;
			}
			else //not first iteration
			{
				sLayout.putConstraint(SpringLayout.NORTH, newRowPanel, 3, SpringLayout.SOUTH, previousRowPanel);
				
				panelHeight += 33;
			}
			
			previousRowPanel = newRowPanel;//remember for next iteration
			
			newPanelMain.add(newRowPanel);
			
			holder.getBDelete().addActionListener(this);
			holder.getBDetails().addActionListener(this);
		}//end for
		
		newPanelMain.setPreferredSize(new Dimension(sWidth-20, panelHeight));
		
		setTitle("Alert List - " + listAlerts.size() + " active alerts");
		
		return newPanelMain;
		
	}
	
	public void refreshDisplay()
	{
		removeActionListeners();
		
		Container container = getContentPane();
		
		Component[] comps = container.getComponents();
		
		int index = 0;
		
		while(index<comps.length)
		{
			if(comps[index] == panelMain || comps[index] == jsp)
			{
				container.remove(index);
			}
			index++;
		}
		
		panelMain = createMainPanel();
		
		jsp = new JScrollPane(panelMain, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(sWidth-20, JSP_HEIGHT));
		
		panelMain.revalidate();
		container.add(jsp);
		container.validate();
		validate();
	}
	
	/**
	 * removes the action listeners associated with this class
	 */
	public void removeActionListeners()
	{
		for(AlertHolder holder: listAlertHolders)
		{
			holder.getBDelete().removeActionListener(this);
			holder.getBDetails().removeActionListener(this);
		}
	}

}
