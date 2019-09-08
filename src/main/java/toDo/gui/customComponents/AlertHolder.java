package toDo.gui.customComponents;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.*;

import toDo.utilities.*;
import toDo.alert.*;
import toDo.data.ToDoItem;

public class AlertHolder 
{
	private Alert alert;
	private JTextField tDate, tType, tNotify, tToDo;
	private JButton bDetails, bDelete;
	private List<ToDoItem> listToDos;
	public static final int rowHeight = 30;
	
	public AlertHolder(Alert a, List<ToDoItem> listToDos)
	{
		this.listToDos = listToDos;
		alert = a;
		
		bDetails = new JButton("Details");
		bDelete = new JButton("Delete");
		
		tDate = new JTextField();
		tType = new JTextField();
		tNotify = new JTextField();
		tToDo = new JTextField();
		
		bDetails.setPreferredSize(new Dimension(80, rowHeight-5));
		bDelete.setPreferredSize(new Dimension(70, rowHeight-5));
		
		tDate.setPreferredSize(new Dimension(90, rowHeight-5));
		tType.setPreferredSize(new Dimension(75, rowHeight-5));
		tNotify.setPreferredSize(new Dimension(40, rowHeight-5));
		tToDo.setPreferredSize(new Dimension(300, rowHeight-5));
		
		tDate.setEditable(false);
		tType.setEditable(false);
		tNotify.setEditable(false);
		tToDo.setEditable(false);
		
		tDate.setBackground(Color.WHITE);
		tType.setBackground(Color.WHITE);
		tNotify.setBackground(Color.WHITE);
		tToDo.setBackground(Color.WHITE);
		
		updateTDate();
		updateTType();
		updateTNotify();
		updateTTodo();
	}
	
	public void updateTDate()
	{
		tDate.setText(ToDoUtilities.formatDateToDD_MM_YY_hh_mm(alert.getAlertTime()));
	}
	
	public void updateTType()
	{
		if (alert instanceof PriorityAlert)
		{
			tType.setText("Priority");
		}
		else if(alert instanceof MessageAlert)
		{
			tType.setText("Message");
		}
	}
	
	public void updateTNotify()
	{
		if(alert.isNotifier())
		{
			tNotify.setText("Yes");
		}
		else
		{
			tNotify.setText("No");
		}
	}
	
	public void updateTTodo()
	{
		tToDo.setText(ToDoUtilities.getToDoByID(alert.getToDoID(), listToDos).getDescription());
	}
	
	public JPanel getJPanel()
	{
		JPanel jPanel = new JPanel();
		
		jPanel.setPreferredSize(new Dimension(700, rowHeight));
		
		jPanel.add(tDate);
		jPanel.add(tType);
		jPanel.add(tNotify);
		jPanel.add(tToDo);
		jPanel.add(bDetails);
		jPanel.add(bDelete);
		
		return jPanel;
	}

	public JButton getBDelete() {
		return bDelete;
	}

	public JButton getBDetails() {
		return bDetails;
	}

	public Alert getAlert() {
		return alert;
	}
	
}
