package toDo.gui.main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import toDo.gui.customComponents.ToDoHolder;
import toDo.interfaces.*;
import javax.swing.*;
import javax.swing.border.LineBorder;


public class LogToDo extends JFrame implements ActionListener
{
	private static final long serialVersionUID = -5545406794821718167L;
	private int sWidth = 500,sHeight = 620;
	private ToDoHolder holder;
	private JPanel panelHeader, panelHistory, panelAppend;
	private JLabel lHeader, lHistory;
	private JTextArea taHistory, taAppend;
	private JButton bAppend, bClose;
	private ToDoDisplayer sourceWindow;
	
	public LogToDo(ToDoHolder holder, MainGui m)
	{
		super("To Do Log");	//form heading
		//create container to place components in:
		Container container = getContentPane();
		container.setLayout(new FlowLayout());	//set flow layout
		
		//store references
		this.holder = holder;
		sourceWindow = m;
		
		panelHeader = new JPanel();
		panelHistory = new JPanel();
		panelAppend = new JPanel();
		
		panelHeader.setPreferredSize(new Dimension(470,50));
		panelHistory.setPreferredSize(new Dimension(470,300));
		panelAppend.setPreferredSize(new Dimension(470,205));
		
		panelHeader.setBorder(new LineBorder(Color.BLACK));
		panelHistory.setBorder(new LineBorder(Color.BLACK));
		panelAppend.setBorder(new LineBorder(Color.BLACK));
		
		/*
		 * header panel
		 */
		
		String desc = holder.getDescription();
		
		if (desc.length() > 20)
		{
			desc = desc.substring(0,18) + "...";
		}
		
		lHeader = new JLabel("Log for : " + desc);
		
		lHeader.setFont(new Font("Comic Sans MS" , Font.BOLD, 24));
		
		panelHeader.add(lHeader);
		
		/*
		 * history panel
		 */
		
		lHistory = new JLabel("Log History :");
		
		taHistory = new JTextArea();
		
		//taHistory.setPreferredSize(new Dimension(410,5000));
		taHistory.setLineWrap(true);
		taHistory.setWrapStyleWord(true);
		taHistory.setEditable(false);
		taHistory.setText(holder.getLog());
		
		JScrollPane jsp = new JScrollPane(taHistory);
		jsp.setPreferredSize(new Dimension(450,260));
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		panelHistory.add(lHistory);
		panelHistory.add(jsp, FlowLayout.CENTER);
		
		/*
		 * append panel
		 */
		
		taAppend = new JTextArea();
		
		//taAppend.setPreferredSize(new Dimension(410,1000));
		taAppend.setLineWrap(true);
		taAppend.setWrapStyleWord(true);
		
		bAppend = new JButton("Append");
		bClose = new JButton("Close");
		
		Dimension dButton = new Dimension(220,25);
		
		bAppend.setPreferredSize(dButton);
		bClose.setPreferredSize(dButton);
		
		bAppend.addActionListener(this);
		bClose.addActionListener(this);
		
		jsp = new JScrollPane(taAppend);
		jsp.setPreferredSize(new Dimension(450,160));
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		panelAppend.add(jsp);
		panelAppend.add(bAppend);
		panelAppend.add(bClose);
		
		/*
		 * add panels to container
		 */
		
		container.add(panelHeader);
		container.add(panelHistory);
		container.add(panelAppend);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();	//get screen resoloution
		setLocation((d.width-sWidth)/2, (d.height-sHeight)/2);	//centre form
		
		setSize(sWidth,sHeight);	//set form size
		setVisible(true);//display screen
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == bAppend)
		{
			String appText = taAppend.getText().trim();
			
			if(appText.length() < 1)
			{
				JOptionPane.showMessageDialog(null, "You can't append an empty message!", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else //append comments
			{
				holder.addToLog(ToDoHolder.LOG_COMMENT, appText);
				taHistory.setText(holder.getLog());
				taAppend.setText("");
				taAppend.requestFocus();
				sourceWindow.refreshToDoDisplay();
			}
		}
		
		if (e.getSource() == bClose)
		{
			String appText = taAppend.getText().trim();
			
			if(appText.equals(""))
			{
				dispose();
			}
			else
			{
				int optionPicked = JOptionPane.showConfirmDialog(this, "You have not saved your changes, close anyway?", "Unsaved Changes", JOptionPane.YES_NO_OPTION);
				
				if (optionPicked == JOptionPane.YES_OPTION)
				{
					dispose();
				}
				
			}
		}
	}	
}
