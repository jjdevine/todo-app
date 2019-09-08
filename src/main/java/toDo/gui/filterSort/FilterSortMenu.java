package toDo.gui.filterSort;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import toDo.interfaces.ToDoDisplayer;
import toDo.interfaces.ToDoFilter;
import toDo.interfaces.ToDoSort;
import toDo.utilities.ToDoUtilities;

public class FilterSortMenu extends JFrame implements ActionListener
{
	private static final long serialVersionUID = -3317296393910157565L;
	ToDoDisplayer sourceWindow;
	private int sWidth = 800, sHeight = 465;
	private JPanel panelHeader, panelFilters, panelSorts, panelButtons;
	private JPanel panelSortList, subPanelSortList, subPanelSortButtons;
	private JLabel lHeader, lFilters, lSorts;
	private JList listFilters, listSorts;
	private DefaultListModel dlmFilters, dlmSorts;
	private JButton bAddFilter, bRemoveFilter, bAddSort, bRemoveSort;
	private JButton bIncreaseSortPriority, bDecreaseSortPriority;
	private JButton bApply, bClose;
	private List<ToDoFilter> filterList;
	private List<ToDoSort> sortList;
	private JScrollPane jsp;
	private Dimension dButtonAddRemoveSize = new Dimension(300,30);
	
	public FilterSortMenu(ToDoDisplayer sourceWindow)
	{
		super("Set Filters and Sorts");	//form heading
		//create container to place components in:
		Container container = getContentPane();
		container.setLayout(new FlowLayout());	//set flow layout
		
		//save references
		this.sourceWindow = sourceWindow;
		
		filterList = sourceWindow.getFilters();
		sortList = sourceWindow.getSorts();
		
		/*
		 * Do panels
		 */
		
		panelHeader = new JPanel();
		panelFilters = new JPanel();
		panelSorts = new JPanel();
		panelButtons = new JPanel();
		
		panelHeader.setPreferredSize(new Dimension(770,50));
		panelFilters.setPreferredSize(new Dimension(405,310));
		panelSorts.setPreferredSize(new Dimension(355,310));
		panelButtons.setPreferredSize(new Dimension(770,42));
		
		panelHeader.setBorder(new LineBorder(Color.BLACK));
		panelFilters.setBorder(new LineBorder(Color.BLACK));
		panelSorts.setBorder(new LineBorder(Color.BLACK));
		panelButtons.setBorder(new LineBorder(Color.BLACK));
		
		/*
		 * header panel
		 */
		
		lHeader = new JLabel("Create Filters & Sorts");
		lHeader.setFont(new Font("Comic Sans MS" , Font.BOLD, 24));
		
		panelHeader.add(lHeader);
		
		/*
		 * Filter Panel
		 */
		
		lFilters = new JLabel("Filters");
		lFilters.setFont(new Font("Comic Sans MS" , Font.BOLD, 16));
		
		dlmFilters = createDLM(filterList);
		listFilters = new JList(dlmFilters);
		//listFilters.setFixedCellWidth(150);
		//listFilters.setVisibleRowCount(filterList.size());
		
		jsp = new JScrollPane(listFilters, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(370, 200));
		
		bAddFilter = new JButton("Add Filter...");
		bRemoveFilter = new JButton("Remove Filter");
		
		bAddFilter.setPreferredSize(dButtonAddRemoveSize);
		bRemoveFilter.setPreferredSize(dButtonAddRemoveSize);
		
		bAddFilter.addActionListener(this);
		bRemoveFilter.addActionListener(this);
		
		panelFilters.add(lFilters);
		panelFilters.add(jsp);
		panelFilters.add(bAddFilter);
		panelFilters.add(bRemoveFilter);
			
		/*
		 * Sort Panel - includes some sub panels for layout purposes
		 */
		
		lSorts = new JLabel("Sorts");
		lSorts.setFont(new Font("Comic Sans MS" , Font.BOLD, 16));
		
		//this panel fits into panelSorts
		panelSortList = new JPanel();
		panelSortList.setPreferredSize(new Dimension(330,200));
		
		//these two panels fit into panelSortList
		subPanelSortList = new JPanel();
		subPanelSortList.setPreferredSize(new Dimension(280, 190));
		subPanelSortButtons = new JPanel();
		subPanelSortButtons.setPreferredSize(new Dimension(40, 190));
		
		dlmSorts = createDLM(sortList);
		listSorts = new JList(dlmSorts);
		//listSorts.setFixedCellWidth(150);
		//listSorts.setVisibleRowCount(sortList.size());
		
		jsp = new JScrollPane(listSorts, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(270, 180));
		
		//try to load images
		java.net.URL imageURLup = FilterSortMenu.class.getResource("up_arrow.gif");
		java.net.URL imageURLdown = FilterSortMenu.class.getResource("down_arrow.gif");	
		
		/*
		 * apply images
		 */
		if (imageURLup == null)
		{
			bIncreaseSortPriority = new JButton();
		}
		else
		{
			bIncreaseSortPriority = new JButton(new ImageIcon(imageURLup));
		}
		
		if (imageURLdown == null)
		{
			bDecreaseSortPriority = new JButton();
		}
		else
		{
			bDecreaseSortPriority = new JButton(new ImageIcon(imageURLdown));
		}
		
		Dimension dImageButton = new Dimension(30,30);
		
		bIncreaseSortPriority.setPreferredSize(dImageButton);
		bDecreaseSortPriority.setPreferredSize(dImageButton);
		
		bIncreaseSortPriority.addActionListener(this);
		bDecreaseSortPriority.addActionListener(this);
		
		SpringLayout sLayout = new SpringLayout();
		sLayout.putConstraint(SpringLayout.NORTH, bIncreaseSortPriority, 30, SpringLayout.NORTH, subPanelSortButtons);
		sLayout.putConstraint(SpringLayout.SOUTH, bDecreaseSortPriority, -30, SpringLayout.SOUTH, subPanelSortButtons);
		
		subPanelSortButtons.setLayout(sLayout);
		
		bAddSort = new JButton("Add Sort...");
		bRemoveSort = new JButton("Remove sort");
		
		bAddSort.setPreferredSize(dButtonAddRemoveSize);
		bRemoveSort.setPreferredSize(dButtonAddRemoveSize);
		
		bAddSort.addActionListener(this);
		bRemoveSort.addActionListener(this);
		
		//stack components onto panel
		
		panelSorts.add(lSorts);
		
		subPanelSortButtons.add(bIncreaseSortPriority);
		subPanelSortButtons.add(bDecreaseSortPriority);
		
		subPanelSortList.add(jsp);
		
		panelSortList.add(subPanelSortList);
		panelSortList.add(subPanelSortButtons);
		
		panelSorts.add(panelSortList);
		
		panelSorts.add(bAddSort);
		panelSorts.add(bRemoveSort);		
		
		/*
		 * Button panel
		 */
		
		bApply = new JButton("Apply");
		bClose = new JButton("Close");
		
		bApply.setPreferredSize(new Dimension(370,30));
		bClose.setPreferredSize(new Dimension(370,30));
		
		bApply.addActionListener(this);
		bClose.addActionListener(this);
		
		panelButtons.add(bApply);
		panelButtons.add(bClose);
		
		/*
		 * add panels to container
		 */
		
		container.add(panelHeader);
		container.add(panelFilters);
		container.add(panelSorts);
		container.add(panelButtons);
		
		/*
		 * Rendering:
		 */
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();	//get screen resoloution
		setLocation((d.width-sWidth)/2, (d.height-sHeight)/2);	//centre form
		
		setSize(sWidth,sHeight);	//set form size
		setVisible(true);//display screen
	}
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == bAddFilter)
		{
			new FilterChooser(this);
		}
		else if(e.getSource() == bRemoveFilter)
		{
			removeFilter(listFilters.getSelectedIndices());
		}
		else if(e.getSource() == bIncreaseSortPriority)
		{
			int index = listSorts.getSelectedIndex();
			
			if(index>0)//dont allow illegal modifications
			{
				ToDoSort tempSort = sortList.get(index);
				sortList.remove(index);
				sortList.add(index - 1, tempSort);
				refreshSortDLM();
				listSorts.setSelectedIndex(index-1);
			}
		}
		else if(e.getSource() == bDecreaseSortPriority)
		{
			int index = listSorts.getSelectedIndex();
			
			if(index != sortList.size()-1)//dont allow illegal modifications
			{
				ToDoSort tempSort = sortList.get(index);
				sortList.remove(index);
				sortList.add(index + 1, tempSort);
				refreshSortDLM();
				listSorts.setSelectedIndex(index+1);
			}
		}
		else if(e.getSource() == bAddSort)
		{
			new SortChooser(this);
		}
		else if(e.getSource() == bRemoveSort)
		{
			removeSort(listSorts.getSelectedIndices());
		}
		else if(e.getSource() == bApply)
		{
			sourceWindow.setFilters(filterList);
			/*
			 * reverse sort list, this means that the sorts at the top of the list
			 * are given the highest priority, rather than being overridden by
			 * the ones below it 
			 */ 
			sourceWindow.setSorts(ToDoUtilities.reverse(sortList));
			dispose();
		}
		else if(e.getSource() == bClose)
		{
			dispose();
		}
		
	}
	
	/**
	 * create DefaultListModel from a list
	 * @return
	 */
	public DefaultListModel createDLM(List list)
	{
		DefaultListModel dlm = new DefaultListModel();
		
		if (list != null)
		{
			Iterator i = list.iterator();
			
			while(i.hasNext())
			{	
				dlm.addElement(i.next());
			}
		}
		
		return dlm;
	}
	
	public void addFilter(ToDoFilter f)
	{
		if (f != null)
		{
			filterList.add(f);
			
			DefaultListModel newDLM = toListModel(filterList);
			
			listFilters.setModel(newDLM);
		}
		
	}
	
	/**
	 * puts items into a default list model using their string value
	 * @param list
	 * @return
	 */
	public DefaultListModel toListModel(List list)
	{
		DefaultListModel newDLM = new DefaultListModel();
		
		Iterator i = list.iterator();
		
		while(i.hasNext())
		{
			newDLM.addElement(i.next().toString());
		}
		
		return newDLM;
	}
	
	/**
	 * removes filters from list according to indices given, gui list then 
	 * refreshed
	 */
	public void removeFilter(int... index)
	{	
		if(index.length < 1)
		{
			//do nothing, nothing selected
		}
		else
		{
			//iterate list backwards to remove selected indices
			for(int x = index.length-1; x>=0; x--)
			{
				filterList.remove(index[x]);
			}
		}
		
		refreshFilterDLM();
	}
	
	/**
	 * refreshes the filter DefaultListModel
	 *
	 */
	public void refreshFilterDLM()
	{
		listFilters.setModel(toListModel(filterList));
	}
	
	/**
	 * add a new sort
	 * @param f
	 */
	public void addSort(ToDoSort s)
	{
		if (s != null)
		{
			sortList.add(s);
			
			DefaultListModel newDLM = toListModel(sortList);
			
			listSorts.setModel(newDLM);
		}
		
	}
	
	/**
	 * removes sorts from list according to indices given, gui list then 
	 * refreshed
	 */
	public void removeSort(int... index)
	{
		if(index.length < 1)
		{
			//do nothing, nothing selected
		}
		else
		{
			//iterate list backwards to remove selected indices
			for(int x = index.length-1; x>=0; x--)
			{
				sortList.remove(index[x]);
			}
		}
		
		refreshSortDLM();
	}
	
	/**
	 * refreshes the sort DefaultListModel
	 *
	 */
	public void refreshSortDLM()
	{
		listSorts.setModel(toListModel(sortList));
	}

}
