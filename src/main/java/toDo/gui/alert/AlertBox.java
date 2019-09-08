package toDo.gui.alert;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class AlertBox extends JFrame implements ActionListener {

	private static final long serialVersionUID = 3452052001577435853L;

	private int sWidth = 300, sHeight = 290;
	private JTextArea textArea;
	private JButton bOK;
	
	public AlertBox(String message)
	{
		super("Alert");	//form heading
		//create container to place components in:
		Container container = getContentPane();
		container.setLayout(new FlowLayout());	//set flow layout
		
		/*
		 * create components
		 */
		
		textArea = new JTextArea(message);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		
		bOK = new JButton("OK");
		bOK.setPreferredSize(new Dimension(280, 30));
		bOK.addActionListener(this);
		
		/*
		 * add components to container
		 */
		
		JScrollPane jsp = new JScrollPane(textArea);
		jsp.setBorder(new LineBorder(Color.BLACK));
		jsp.setPreferredSize(new Dimension(280,200));
		
		container.add(jsp);
		container.add(bOK);
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();	//get screen resoloution
		setLocation((d.width-sWidth)/2, (d.height-sHeight)/2);	//centre form
		
		setSize(sWidth,sHeight);	//set form size
		setVisible(true);//display screen
		
		//make sure user sees alert:
		setAlwaysOnTop(true);
		
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == bOK)
		{
			dispose();
		}
	}

}
