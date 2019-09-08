package toDo.gui.filterSort;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.*;
import javax.swing.border.LineBorder;

import toDo.exceptions.DateParseException;
import toDo.filters.DateFilter;
import toDo.gui.customComponents.DateInput;

public class DateFilterConfigure extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 6659115873073187319L;
	private FilterSortMenu sourceWindow;
	private int sWidth = 500, sHeight = 360;
	private JLabel lFilterBy, lInfo, lBefore, lAfter;
	private JComboBox comboFilterBy;
	private DateInput diBefore, diAfter;
	private JButton bConfirm, bCancel;
	private JPanel panelCombo, panelDates, panelButtons;
	
	public DateFilterConfigure(FilterSortMenu sourceWindow)
	{
		super("Create New Filter");	//form heading
		//create container to place components in:
		Container container = getContentPane();
		container.setLayout(new FlowLayout());	//set flow layout
		
		/*
		 * save references
		 */
		
		this.sourceWindow = sourceWindow;
		
		/*
		 * do panels
		 */
		
		panelCombo = new JPanel();
		panelDates = new JPanel();
		panelButtons = new JPanel();
		
		panelCombo.setPreferredSize(new Dimension(470,60));
		panelDates.setPreferredSize(new Dimension(470,200));
		panelButtons.setPreferredSize(new Dimension(470,40));
		
		panelCombo.setBorder(new LineBorder(Color.BLACK));
		panelDates.setBorder(new LineBorder(Color.BLACK));
		panelButtons.setBorder(new LineBorder(Color.BLACK));
		
		/*
		 * panel combo
		 */
		
		lFilterBy = new JLabel("Filter by : ");
		
		String[] options = {"Create Date", "Last Modified Date"};
		
		comboFilterBy = new JComboBox(options);
		
		comboFilterBy.setPreferredSize(new Dimension(140,30));
		
		panelCombo.add(lFilterBy);
		panelCombo.add(comboFilterBy);
		
		/*
		 * panel Dates
		 */
		
		JPanel panelInfo = new JPanel();
		panelInfo.setPreferredSize(new Dimension(400,50));
		
		lInfo = new JLabel("*Enter a date in 'Before', 'After' or both");
		lInfo.setFont(new Font("Arial" , Font.PLAIN, 16));
		lInfo.setForeground(Color.RED);
		panelInfo.add(lInfo);
		
		lBefore = new JLabel("Before : ");
		lBefore.setFont(new Font("Comic Sans MS" , Font.BOLD, 18));
		lBefore.setPreferredSize(new Dimension(100,50));
		
		diBefore = new DateInput();
		
		lAfter = new JLabel("After : ");
		lAfter.setFont(new Font("Comic Sans MS" , Font.BOLD, 18));
		lAfter.setPreferredSize(new Dimension(100,50));
		
		diAfter = new DateInput();
		
		panelDates.add(panelInfo);
		panelDates.add(lBefore);
		panelDates.add(diBefore.getComponent());
		panelDates.add(lAfter);
		panelDates.add(diAfter.getComponent());
		
		/*
		 * panel Buttons
		 */
		
		bConfirm = new JButton("Confirm Filter");
		bCancel = new JButton("Cancel");
		
		Dimension dButtonSize = new Dimension(200,30);
		
		bConfirm.setPreferredSize(dButtonSize);
		bCancel.setPreferredSize(dButtonSize);
		
		bConfirm.addActionListener(this);
		bCancel.addActionListener(this);
		
		panelButtons.add(bConfirm);
		panelButtons.add(bCancel);
		
		/*
		 * add panels to container
		 */
		
		container.add(panelCombo);
		container.add(panelDates);
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
		if (e.getSource() == bConfirm)
		{
			DateFilter filter = buildFilter();
			
			if(filter != null)
			{
				sourceWindow.addFilter(filter);
				dispose();
			}
		}
		else if (e.getSource() == bCancel)
		{
			dispose();
		}
		
	}
	
	public DateFilter buildFilter()
	{
		int selection = comboFilterBy.getSelectedIndex();
		
		int filterType;
		
		Calendar cBefore;
		Calendar cAfter;
		
		try
		{
			/*
			 * read dates in
			 */
			cBefore = diBefore.getDate();
			cAfter = diAfter.getDate();		
		}
		catch(DateParseException ex)
		{
			JOptionPane.showMessageDialog(null, "Problem reading dates : " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		if(cBefore == null && cAfter == null) //no dates provided
		{
			JOptionPane.showMessageDialog(null, "Provide at least one date!", "Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		if(cBefore == null) //only after date provided
		{
			filterType = DateFilter.FILTER_TYPE_AFTER;
		}
		else if(cAfter == null) //only before date provided
		{
			filterType = DateFilter.FILTER_TYPE_BEFORE;
		}
		else //both dates provided
		{
			filterType = DateFilter.FILTER_TYPE_BETWEEN;
		}
		
		/*
		 * create datefilter object
		 */
		
		DateFilter f;
		
		if(selection == 0)
		{
			f = new DateFilter(filterType, DateFilter.FILTER_BY_CREATE_DATE);
		}
		else //should be 1 in this case
		{
			f = new DateFilter(filterType, DateFilter.FILTER_BY_LAST_MODIFIED_DATE);
		}
		
		/*
		 * apply filter dates
		 */
		
		if (filterType == DateFilter.FILTER_TYPE_AFTER)
		{
			f.applyFilterDate(cAfter);
		}
		else if (filterType == DateFilter.FILTER_TYPE_BEFORE)
		{
			f.applyFilterDate(cBefore);
		}
		else if (filterType == DateFilter.FILTER_TYPE_BETWEEN)
		{
			f.applyFilterDate(cAfter,cBefore);
		}
		
		return f;
		
	}

}
