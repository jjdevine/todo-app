package toDo.gui.filterSort;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class FilterChooser extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 5892528749810829425L;
	private int sWidth = 250, sHeight = 185;
	private JButton bDate, bPriority, bDescription, bCancel;
	private Dimension dButtonSize = new Dimension(230,30);
	private FilterSortMenu sourceWindow;
	
	public FilterChooser(FilterSortMenu sourceWindow)
	{
		super("Filter Type...");	//form heading
		//create container to place components in:
		Container container = getContentPane();
		container.setLayout(new FlowLayout());	//set flow layout
		
		this.sourceWindow = sourceWindow;
		
		bDate = new JButton("Filter by Date");
		bPriority = new JButton("Filter by Priority");
		bDescription = new JButton("Filter by Description");
		bCancel = new JButton("Cancel");
		
		bDate.setPreferredSize(dButtonSize);
		bPriority.setPreferredSize(dButtonSize);
		bDescription.setPreferredSize(dButtonSize);
		bCancel.setPreferredSize(dButtonSize);
		
		bDate.addActionListener(this);
		bPriority.addActionListener(this);
		bDescription.addActionListener(this);
		bCancel.addActionListener(this);
		
		container.add(bDate);
		container.add(bPriority);
		container.add(bDescription);
		container.add(bCancel);
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();	//get screen resoloution
		setLocation((d.width-sWidth)/2, (d.height-sHeight)/2);	//centre form
		
		setSize(sWidth,sHeight);	//set form size
		setVisible(true);//display screen
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == bDate)
		{
			new DateFilterConfigure(sourceWindow);
			dispose();
		}
		else if (e.getSource() == bPriority)
		{
			new PriorityFilterConfigure(sourceWindow);
			dispose();
		}
		else if (e.getSource() == bDescription)
		{
			new DescriptionFilterConfigure(sourceWindow);
			dispose();
		}
		else if (e.getSource() == bCancel)
		{
			dispose();
		}
		
	}
	
}
