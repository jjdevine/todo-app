package toDo.gui.filterSort;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import toDo.sorts.*;

public class SortChooser extends JFrame implements ActionListener
{

	private static final long serialVersionUID = 3090605465447795148L;
	private int sWidth = 250, sHeight = 185;
	private JButton bDate, bDescription, bPriority, bCancel;
	private FilterSortMenu sourceWindow;
	private Dimension dButtonSize = new Dimension(230,30);
	
	public SortChooser(FilterSortMenu sourceWindow)
	{
		super("New Sort...");	//form heading
		//create container to place components in:
		Container container = getContentPane();
		container.setLayout(new FlowLayout());	//set flow layout
		
		//save references
		this.sourceWindow = sourceWindow;
		
		/*
		 * do buttons
		 */
		
		bDate = new JButton("Sort by Date");
		bPriority = new JButton("Sort by Priority");
		bDescription = new JButton("Sort by Description");
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
			Object[] possibleTypes = { "Create Date", "Last Modified Date" };
			String selectedType = (String)JOptionPane.showInputDialog(this,"Choose date to sort by...", "Input Parameter",	JOptionPane.INFORMATION_MESSAGE, null,	possibleTypes, possibleTypes[0]);
			
			if(selectedType != null)
			{
				Object[] possibleOrders = {"Descending (most recent at top)", "Ascending (oldest at top)" };
				String selectedOrder = (String)JOptionPane.showInputDialog(this,"Choose style of sort...", "Input Order",	JOptionPane.INFORMATION_MESSAGE, null,	possibleOrders, possibleOrders[0]);
				
				if(selectedOrder != null)//values now provided for both required parameters
				{
					DateSort sort;
					
					int type;
					int order;
					
					if(selectedType.equals("Create Date"))
					{
						type = DateSort.SORT_BY_CREATE_DATE;
					}
					else
					{
						type = DateSort.SORT_BY_LAST_MODIFIED_DATE;
					}
					
					if(selectedOrder.equals("Ascending (oldest at top)"))
					{
						order = DateSort.SORT_OLDEST_TO_START;
					}
					else
					{
						order = DateSort.SORT_NEWEST_TO_START;
					}
					
					/*
					 * create new sort and return it to source window
					 */
					
					sort = new DateSort(type,order);
					
					sourceWindow.addSort(sort);
				}
			}
			
			
			dispose();
			
			
		}
		else if (e.getSource() == bPriority)
		{
			
			Object[] possibleOrders = { "Descending (highest priority at top)", "Ascending (lowest priority at top)" };
			String selectedOrder = (String)JOptionPane.showInputDialog(this,"Choose style of sort...", "Input Order",	JOptionPane.INFORMATION_MESSAGE, null,	possibleOrders, possibleOrders[0]);
			
			if(selectedOrder != null)//values now provided for both required parameters
			{
				PrioritySort sort;
				
				int order;
				
				if(selectedOrder.equals("Ascending (lowest priority at top)"))
				{
					order = PrioritySort.SORT_ASCENDING;
				}
				else
				{
					order = PrioritySort.SORT_DESCENDING;
				}
				
				/*
				 * create new sort and return it to source window
				 */
				
				sort = new PrioritySort(order);
				
				sourceWindow.addSort(sort);
			}
			
			dispose();
			
		}
		else if (e.getSource() == bDescription)
		{
			Object[] possibleOrders = { "Descending (A-Z)", "Ascending (Z-A)"};
			String selectedOrder = (String)JOptionPane.showInputDialog(this,"Choose style of sort...", "Input Order",	JOptionPane.INFORMATION_MESSAGE, null,	possibleOrders, possibleOrders[0]);
			
			if(selectedOrder != null)//values now provided for both required parameters
			{
				DescriptionSort sort;
				
				int order;
				
				if(selectedOrder.equals("Descending (A-Z)"))
				{
					order = DescriptionSort.SORT_DESCENDING;
				}
				else
				{
					order = PrioritySort.SORT_ASCENDING;
				}
				
				/*
				 * create new sort and return it to source window
				 */
				
				sort = new DescriptionSort(order);
				
				sourceWindow.addSort(sort);
			}
			
			dispose();
		}
		else if (e.getSource() == bCancel)
		{
			dispose();
		}
	}

}
