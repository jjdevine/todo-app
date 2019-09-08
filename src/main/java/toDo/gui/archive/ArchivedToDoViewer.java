package toDo.gui.archive;

import java.awt.Container;
import java.util.*;

import toDo.sorts.DescriptionSort;
import toDo.utilities.*;
import toDo.data.*;
import toDo.gui.customComponents.ToDoArchivedHolder;
import toDo.gui.customComponents.ToDoHolder;
import toDo.gui.filterSort.FilterSortMenu;
import toDo.gui.main.MainGui;
import toDo.interfaces.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class ArchivedToDoViewer extends JFrame implements ActionListener, ToDoDisplayer
{

	private static final long serialVersionUID = -1268167596704584087L;
	private int sWidth = 800, sHeight = 750;
	private  List<ToDoArchivedHolder> toDoArchive = new ArrayList<ToDoArchivedHolder>();
	private  List<ToDoArchivedHolder> toDoArchive_Filtered_Sorted = new ArrayList<ToDoArchivedHolder>();
	private  File archiveFile = null;
	private  JPanel panelHeadings, panelMain;
	private  JLabel lPriority, lDescription, lCreateDate, lLastModified;
	private  Container container;
	private  JScrollPane jsp;
	private  MainGui main;
	private  int adjustment = 0;
	private  JMenuBar menuBar;
	private  JMenu optionsMenu, fileMenu;
	private  JMenuItem closeMenuItem, deleteMenuItem, filteringOrderingMenuItem;
	private  JMenuItem newArchiveMenuItem;
	private  List<ToDoFilter> listFilters = new ArrayList<ToDoFilter>();
	private  List<ToDoSort> listSorts = new ArrayList<ToDoSort>();
	private  boolean usingCurrentArchive = true;
	
	/**
	 * constructor designed for use when accessing archives other than the 
	 * default.
	 * @param m
	 * @param f
	 */
	
	public ArchivedToDoViewer(MainGui m, File f)
	{
		super("To Do Archive");	//form heading
		
		//create container to place components in:
		container = getContentPane();
		container.setLayout(new FlowLayout());	//set flow layout
		
		/*
		 * make sizings appropriate for resolution
		 */
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();	//get screen resoloution
		
		if(d.height <= 850)
		{
			adjustment = 200;
		}
		
		//save reference
		main = m;
		
		/*
		 * load default sorts
		 */
		
		createDefaultSorts();
		
		/*
		 * load archived todos
		 */
		
		if (f!=null) //alternative file supplied
		{
			archiveFile = f;
			usingCurrentArchive = false;
		}
		else
		{
			archiveFile = main.archiveSaveFile;
		}
		toDoArchive = ToDoFileIO.loadToDosAsArchivedHolderList(archiveFile);
		
		/*
		 * add menu bar
		 */
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		/*
		 * Do FileMenu
		 */
		
		fileMenu = new JMenu("File");
		
		closeMenuItem = new JMenuItem("Close");
		
		menuBar.add(fileMenu);

		fileMenu.add(closeMenuItem);
	
		closeMenuItem.addActionListener(this);
		
		/*
		 * Do FileMenu
		 */
		
		optionsMenu = new JMenu("Options");
		
		deleteMenuItem = new JMenuItem("Delete To Do...");
		filteringOrderingMenuItem = new JMenuItem("Filtering & Sorting...");
		newArchiveMenuItem = new JMenuItem("Isolate this Archive...");
		
		menuBar.add(optionsMenu);

		optionsMenu.add(deleteMenuItem);
		optionsMenu.add(filteringOrderingMenuItem);
		optionsMenu.add(newArchiveMenuItem);
	
		deleteMenuItem.addActionListener(this);
		filteringOrderingMenuItem.addActionListener(this);
		newArchiveMenuItem.addActionListener(this);
				
		/*
		 * do panels
		 */
		
		panelHeadings = new JPanel();
		panelMain = createNewMainPanel();
		
		panelHeadings.setPreferredSize(new Dimension(780,25));
		
		panelHeadings.setBorder(new LineBorder(Color.BLACK));
		
		/*
		 * header panel
		 */
		
		lPriority = new JLabel("Priority  ",JLabel.CENTER);
		lDescription = new JLabel("Description           ",JLabel.CENTER);
		lCreateDate = new JLabel("Created On   ",JLabel.CENTER);
		lLastModified = new JLabel("Last Modified  ",JLabel.CENTER);
		
		lPriority.setPreferredSize(new Dimension(80,15));
		lDescription.setPreferredSize(new Dimension(300,15));
		lCreateDate.setPreferredSize(new Dimension(90,15));
		lLastModified.setPreferredSize(new Dimension(90,15));
		
		panelHeadings.add(lPriority);
		panelHeadings.add(lDescription);
		panelHeadings.add(lCreateDate);
		panelHeadings.add(lLastModified);
		
		//dummy labels so that headings align properly
		JLabel lDummy1 = new JLabel(" ");
		lDummy1.setPreferredSize(new Dimension(70,15));
		panelHeadings.add(lDummy1);	
		JLabel lDummy2 = new JLabel(" ");
		lDummy2.setPreferredSize(new Dimension(110,15));
		panelHeadings.add(lDummy2);		
			
		/*
		 * add panels
		 */
		
		jsp = new JScrollPane(panelMain, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(780,640 - adjustment));
		
		container.add(panelHeadings);
		container.add(jsp);
		
		/*
		 * window rendering
		 */
		
		setLocation((d.width-sWidth)/2, (d.height-sHeight+adjustment)/2);	//centre form
		setSize(sWidth,sHeight - adjustment);	//set form size
		setVisible(true);//display screen
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		if(usingCurrentArchive && toDoArchive.size() > 250)
		{
			int optionPicked = JOptionPane.showConfirmDialog(this, "Your current archive has over 250 items in it, it is recommended you isolate this archive, proceed?", "High Volume of To Dos", JOptionPane.YES_NO_OPTION);
			
			if (optionPicked == JOptionPane.YES_OPTION)
			{
				removeToDosToNewArchive();
			}
		}
	}
	
	public JPanel createNewMainPanel()
	{
		JPanel newMainPanel = new JPanel();
		
		newMainPanel.setBorder(new LineBorder(Color.BLACK));
		
		SpringLayout sLayout = new SpringLayout();
		newMainPanel.setLayout(sLayout);
			
		/*
		 * filter & then sort list
		 */
		
		//need to convert to item list, afterwards convert back again
		List<ToDoItem> sortedListAsItems = ToDoUtilities.convertArchivedHolderListToItemList(toDoArchive);
		
		Iterator iFilters = listFilters.iterator();
		
		while(iFilters.hasNext())
		{
			ToDoFilter filter = (ToDoFilter)iFilters.next();
			
			filter.setList(sortedListAsItems);
			
			sortedListAsItems = filter.getFilteredList();
		}
		
		Iterator iSorts = listSorts.iterator();
		
		while(iSorts.hasNext())
		{
			ToDoSort sort = (ToDoSort)iSorts.next();
			
			sort.setList(sortedListAsItems);
			
			sortedListAsItems = sort.getSortedList();
		}
		
		toDoArchive_Filtered_Sorted = ToDoUtilities.convertItemListToArchivedHolderList(sortedListAsItems);
		
		//get iterator
		//Iterator i = toDoArchive.iterator();
		Iterator i = toDoArchive_Filtered_Sorted.iterator();
		
		boolean firstIteration = true;
		JPanel previousRowPanel = null;
		
		int panelHeight = 0;
		
		while(i.hasNext())
		{
			ToDoArchivedHolder holder = (ToDoArchivedHolder)i.next();
			
			JPanel newRowPanel = holder.getPanel();
			
			if(firstIteration)
			{
				sLayout.putConstraint(SpringLayout.NORTH, newMainPanel, 3, SpringLayout.NORTH, newRowPanel);
				firstIteration = false;
				//need to calculate required height of panel
				panelHeight += 38;
			}
			else
			{
				sLayout.putConstraint(SpringLayout.NORTH, newRowPanel, 3, SpringLayout.SOUTH, previousRowPanel);
				//need to calculate required height of panel
				panelHeight += 33;
			}
			
			
			
			previousRowPanel = newRowPanel;
			
			newMainPanel.add(newRowPanel);
			
			//add actionlisteners to buttons for this todo
			holder.getBLog().addActionListener(this);
			holder.getBOpen().addActionListener(this);
			
		}
		
		setTitle("To Do Archive - " + toDoArchive_Filtered_Sorted.size() + " of " + toDoArchive.size() + " archived to do items");
				
		newMainPanel.setPreferredSize(new Dimension(760, panelHeight));
		
		return newMainPanel;
	}
	
	public void refreshToDoDisplay()
	{
		/* 
		 *remove current action listeners - otherwise events fire twice
		 */
		
		removeActionListeners();
		
		//remove previous main panel to free up memory
		Component[] comps = container.getComponents();
		
		int index = 0;
		for(Component c : comps)
		{
			if (c == panelMain)
			{
				container.remove(index);
			}
			else if (c == jsp)
			{
				container.remove(index);
			}
			else
			{
				index++;
			}
		}
		
		panelMain = createNewMainPanel();
		
		jsp = new JScrollPane(panelMain, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(780,640 - adjustment));
		
		panelMain.revalidate();
		container.add(jsp);
		container.validate();
		validate();
		
		/*
		 * save to disk automatically
		 */
		ToDoFileIO.saveToDosAsFile(archiveFile, ToDoUtilities.convertArchivedHolderListToItemList(toDoArchive));
	}
	
	public void removeActionListeners()
	{
		Iterator i = toDoArchive_Filtered_Sorted.iterator();
		
		while (i.hasNext() == true)
		{
			ToDoArchivedHolder holder = (ToDoArchivedHolder)i.next();
			holder.getBLog().removeActionListener(this);
			holder.getBOpen().removeActionListener(this);
		}
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == closeMenuItem)
		{
			listFilters = null;
			listSorts = null;
			toDoArchive = null;
			toDoArchive_Filtered_Sorted = null;
			dispose();
		}
		else if (e.getSource() == deleteMenuItem)
		{
			new DeleteArchivedToDo(toDoArchive_Filtered_Sorted, this);
		}
		else if (e.getSource() == filteringOrderingMenuItem)
		{
			new FilterSortMenu(this);
		}
		else if (e.getSource() == newArchiveMenuItem)
		{
			if (usingCurrentArchive)
			{
				int optionPicked = JOptionPane.showConfirmDialog(this, "Are you sure you want to isolate this archive?", "Sure?", JOptionPane.YES_NO_OPTION);
				
				if(optionPicked == JOptionPane.YES_OPTION)
				{
					removeToDosToNewArchive();
				}
			}
			else
			{
				JOptionPane.showMessageDialog(null, "You can only do this with the current archive", "Invalid Operation", JOptionPane.WARNING_MESSAGE);
			}
		}
		else
		{
			/*
			 * loop to see if buttons on any of the archived items has 
			 * been clicked 
			 */
			Iterator i = toDoArchive_Filtered_Sorted.iterator();
			
			whileloop:
			while(i.hasNext())
			{
				ToDoArchivedHolder holder = (ToDoArchivedHolder)i.next();
				
				if(e.getSource() == holder.getBLog())
				{
					new ArchivedLogToDo(holder.getToDoItem());
					break whileloop;
				}
				else if(e.getSource() == holder.getBOpen())
				{
					//check user really wants to do this:
					int optionPicked = JOptionPane.showConfirmDialog(this, "Really re-open '" + holder.getDescription() + "'?", "Confirm Re-Open?", JOptionPane.YES_NO_OPTION);
					
					if (optionPicked == JOptionPane.YES_OPTION)
					{
						/*
						 * need to:
						 * 1. mark this todo as incomplete
						 * 2. update log of todo
						 * 3. send this file to main form
						 * 4. remove todo from archive list
						 * 5. update save files
						 * 6. update display
						 */
						
						//1.
						holder.markIncomplete();
						
						//2.
						holder.getToDoItem().addToLog(ToDoItem.LOG_AUDIT, "To do re-opened.");
						
						//3. 			
						ToDoHolder activeHolder = new ToDoHolder(holder.getToDoItem());
						//main save file should update as result of below:
						main.addToDo(activeHolder);
						
						//4.
						toDoArchive.remove(ToDoUtilities.getIndexOfArchivedHolderInList(holder, toDoArchive));
						
						//5.
						ToDoFileIO.saveToDosAsFile(archiveFile, ToDoUtilities.convertArchivedHolderListToItemList(toDoArchive));
						
						//6.
						refreshToDoDisplay();
					}
					break whileloop;
				}
			}
		}
	}

	public List getToDoList() 
	{
		
		return toDoArchive;
	}

	public void setFilters(List<ToDoFilter> filters) 
	{	
		listFilters = filters;
		refreshToDoDisplay();
	}

	public void setSorts(List<ToDoSort> sorts) 
	{
		listSorts = sorts;
		refreshToDoDisplay();
	}

	@SuppressWarnings("unchecked")
	public List<ToDoFilter> getFilters() 
	{
		/*
		 * reverse the list as visually the user needs to see the priority of
		 * the sorts, not the order they are executed in
		 */
		return ToDoUtilities.reverse(listFilters);
	}

	@SuppressWarnings("unchecked")
	public List<ToDoSort> getSorts() 
	{	
		/*
		 * reverse the list as visually the user needs to see the priority of
		 * the sorts, not the order they are executed in
		 */
		return ToDoUtilities.reverse(listSorts);
	}
	
	/*
	 * method to create the default sorts on this form
	 */
	private void createDefaultSorts()
	{
		DescriptionSort sort = new DescriptionSort(DescriptionSort.SORT_DESCENDING);
		listSorts.add(sort);
	}
	
	/**
	 * delete a todo completely
	 * @param item
	 */
	public void deleteToDoItem(ToDoItem item)
	{
		int index = ToDoUtilities.getIndexOfArchivedHolderInList(new ToDoArchivedHolder(item), toDoArchive);
		
		toDoArchive.remove(index);
		
		refreshToDoDisplay();		
	}

	public List<ToDoArchivedHolder> getFilteredSortedToDoList() 
	{
		return toDoArchive_Filtered_Sorted;
		
	}
	
	private void removeToDosToNewArchive()
	{	
		//get name for new archive
		File f = ToDoFileIO.getNextArchiveFileName();
		
		//convert list to items
		List<ToDoItem> itemList = ToDoUtilities.convertArchivedHolderListToItemList(toDoArchive);
		
		//save these files to new archive
		ToDoFileIO.saveToDosAsFile(f, itemList);
		
		//create ini file
		String strStart = ToDoUtilities.formatDate(ToDoUtilities.getEarliestLastModifiedDate(itemList, false));
		String strEnd = ToDoUtilities.formatDate(ToDoUtilities.getEarliestLastModifiedDate(itemList, true));
		ToDoFileIO.createIniFile(new File(f.toString() + ".ini"), "start date=" + strStart, "end date=" + strEnd);
		
		//clear archive file
		ToDoFileIO.clearFile(archiveFile);
		
		//advise user that window needs to close
		JOptionPane.showMessageDialog(null, "This window will now close.", "FYI", JOptionPane.INFORMATION_MESSAGE);
		
		dispose();
		
	}

	public List<ToDoItem> getToDoItemList() 
	{
		return ToDoUtilities.convertArchivedHolderListToItemList(toDoArchive);
	}
	

}
