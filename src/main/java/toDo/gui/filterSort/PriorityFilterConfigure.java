package toDo.gui.filterSort;

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

import toDo.filters.PriorityFilter;

public class PriorityFilterConfigure extends JFrame implements ActionListener
{
	
	private static final long serialVersionUID = 3641029475343898160L;
	private int sWidth = 200, sHeight = 260;
	private JPanel panelUrgent, panelHigh, panelMedium, panelLow, panelButtons;
	private JCheckBox cboxUrgent, cboxHigh, cboxMedium, cboxLow;
	private JButton bConfirm, bCancel;
	private FilterSortMenu sourceWindow;
	private Dimension dPanelSize = new Dimension(190,30);
	
	public PriorityFilterConfigure(FilterSortMenu sourceWindow)
	{
		super("Create New Filter");	//form heading
		//create container to place components in:
		Container container = getContentPane();
		container.setLayout(new FlowLayout());	//set flow layout
		
		/*
		 * store references
		 */
		
		this.sourceWindow = sourceWindow;
		
		/*
		 * do panels
		 */
		
		panelUrgent = new JPanel();
		panelHigh = new JPanel();
		panelMedium = new JPanel();
		panelLow = new JPanel();
		panelButtons = new JPanel();
		
		panelUrgent.setPreferredSize(dPanelSize);
		panelHigh.setPreferredSize(dPanelSize);
		panelMedium.setPreferredSize(dPanelSize);
		panelLow.setPreferredSize(dPanelSize);
		panelButtons.setPreferredSize(new Dimension(190,70));
		
		panelButtons.setBorder(new LineBorder(Color.BLACK));
		
		/*
		 * panel urgent
		 */
		
		cboxUrgent = new JCheckBox("Urgent Priority Items");
		cboxUrgent.setSelected(true);
		cboxUrgent.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		
		panelUrgent.add(cboxUrgent);
		
		/*
		 * panel high
		 */
		
		cboxHigh = new JCheckBox("High Priority Items");
		cboxHigh.setSelected(true);
		cboxHigh.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		
		panelHigh.add(cboxHigh);
		
		/*
		 * panel medium
		 */
		
		cboxMedium = new JCheckBox("Medium Priority Items");
		cboxMedium.setSelected(true);
		cboxMedium.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		
		panelMedium.add(cboxMedium);
		
		/*
		 * panel low
		 */
		
		cboxLow = new JCheckBox("Low Priority Items");
		cboxLow.setSelected(true);
		cboxLow.setFont(new Font("Comic Sans MS", Font.PLAIN, 16));
		
		panelLow.add(cboxLow);
		
		/*
		 * panel buttons
		 */
		
		bConfirm = new JButton("Confirm");
		bCancel = new JButton("Cancel");
		
		Dimension dButton = new Dimension(180,25);
		
		bConfirm.setPreferredSize(dButton);
		bCancel.setPreferredSize(dButton);
		
		bConfirm.addActionListener(this);
		bCancel.addActionListener(this);
		
		panelButtons.add(bConfirm);
		panelButtons.add(bCancel);
		
		/*
		 * add panels to container
		 */
		
		container.add(panelUrgent);
		container.add(panelHigh);
		container.add(panelMedium);
		container.add(panelLow);
		container.add(panelButtons);
		
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
		if(e.getSource() == bConfirm)
		{
			//get values
			boolean bUrgent = cboxUrgent.isSelected();
			boolean bHigh = cboxHigh.isSelected();
			boolean bMedium = cboxMedium.isSelected();
			boolean bLow = cboxLow.isSelected();
			
			if(!bUrgent && !bHigh && !bMedium && !bLow) //nothing selected
			{
				JOptionPane.showMessageDialog(null, "You must choose at least one parameter!", "Invalid Selection", JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				PriorityFilter filter = new PriorityFilter();
				
				//specify which priorities to filter out
				if(!bUrgent)
				{
					filter.applyFilter(PriorityFilter.PRIORITY_URGENT);
				}
				if(!bHigh)
				{
					filter.applyFilter(PriorityFilter.PRIORITY_HIGH);
				}
				if(!bMedium)
				{
					filter.applyFilter(PriorityFilter.PRIORITY_MEDIUM);
				}
				if(!bLow)
				{
					filter.applyFilter(PriorityFilter.PRIORITY_LOW);
				}
				
				sourceWindow.addFilter(filter);
				dispose();
			}
		}
		else if (e.getSource() == bCancel)
		{
			dispose();
		}
	}

}
