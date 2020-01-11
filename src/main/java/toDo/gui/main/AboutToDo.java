package toDo.gui.main;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class AboutToDo extends JFrame implements ActionListener
{
	private static final long serialVersionUID = -4549972473170002027L;
	private int sWidth = 200, sHeight = 140;
	private JLabel lVersion, lCreatedBy; 
	private JButton bClose;

	public AboutToDo()
	{
		super("About...");	//form heading
		//create container to place components in:
		Container container = getContentPane();
		container.setLayout(new FlowLayout());	//set flow layout
		
		lVersion = new JLabel("To Do List Version 1.4.2", JLabel.CENTER);
		lCreatedBy = new JLabel("Created By Jonathan Devine", JLabel.CENTER);
		
		lVersion.setPreferredSize(new Dimension(180,25));
		lCreatedBy.setPreferredSize(new Dimension(180,25));
		
		bClose = new JButton("OK");
		bClose.setPreferredSize(new Dimension(100,25));
		bClose.addActionListener(this);
		
		container.add(lVersion);
		container.add(lCreatedBy);
		
		container.add(bClose);
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();	//get screen resoloution
		setLocation((d.width-sWidth)/2, (d.height-sHeight)/2);	//centre form
		
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
