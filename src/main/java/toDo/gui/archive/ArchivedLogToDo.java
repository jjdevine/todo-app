package toDo.gui.archive;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.LineBorder;

import toDo.data.*;

public class ArchivedLogToDo extends JFrame implements ActionListener
{
	private static final long serialVersionUID = -5545406794821718167L;
	private int sWidth = 500,sHeight = 595;
	private ToDoItem item;
	private JPanel panelHeader, panelHistory, panelClose;
	private JLabel lHeader, lHistory;
	private JTextArea taHistory, taAppend;
	private JButton bClose;
	
	public ArchivedLogToDo(ToDoItem newItem)
	{
		super("To Do Log");	//form heading
		//create container to place components in:
		Container container = getContentPane();
		container.setLayout(new FlowLayout());	//set flow layout
		
		//store reference
		item = newItem;
		
		panelHeader = new JPanel();
		panelHistory = new JPanel();
		panelClose = new JPanel();
		
		panelHeader.setPreferredSize(new Dimension(470,50));
		panelHistory.setPreferredSize(new Dimension(470,450));
		panelClose.setPreferredSize(new Dimension(470,35));
		
		panelHeader.setBorder(new LineBorder(Color.BLACK));
		panelHistory.setBorder(new LineBorder(Color.BLACK));
		panelClose.setBorder(new LineBorder(Color.BLACK));
		
		/*
		 * header panel
		 */
		
		String desc = item.getDescription();
		
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
		taHistory.setText(item.getLog().toString());
		
		JScrollPane jsp = new JScrollPane(taHistory);
		jsp.setPreferredSize(new Dimension(450,410));
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
		
		bClose = new JButton("Close");
		
		Dimension dButton = new Dimension(400,25);
		
		bClose.setPreferredSize(dButton);
		
		bClose.addActionListener(this);
		
		jsp = new JScrollPane(taAppend);
		jsp.setPreferredSize(new Dimension(450,160));
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		panelClose.add(bClose);
		
		/*
		 * add panels to container
		 */
		
		container.add(panelHeader);
		container.add(panelHistory);
		container.add(panelClose);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();	//get screen resoloution
		setLocation((d.width-sWidth)/2, (d.height-sHeight)/2);	//centre form
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		setSize(sWidth,sHeight);	//set form size
		setVisible(true);//display screen
	}
	
	public void actionPerformed(ActionEvent e) 
	{
	
		if (e.getSource() == bClose)
		{
			dispose();
		}
	}
	
}
