package toDo.gui.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.Timer;

import toDo.alert.Alert;
import toDo.data.*;
import toDo.events.AutoEscalationCommand;
import toDo.events.ToDoEvent;
import toDo.events.ToDoObserver;
import toDo.gui.customComponents.AlertPane;
import toDo.scheduled.*;
import toDo.sorts.DescriptionSort;
import toDo.sorts.PrioritySort;
import toDo.utilities.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

import toDo.fileFilters.dataFileFilter;
import toDo.gui.alert.AlertViewer;
import toDo.gui.alert.MessageAlertConfigure;
import toDo.gui.alert.PriorityAlertConfigure;
import toDo.gui.archive.ArchivedToDoViewer;
import toDo.gui.customComponents.ToDoHolder;
import toDo.gui.filterSort.FilterSortMenu;
import toDo.interfaces.*;

public class MainGui extends JFrame implements ActionListener, ToDoDisplayer, Alertable, ToDoObserver {

	private static final long serialVersionUID = 53347349340606524L;
	public final File saveFile = new File("./todo.data");
	public final File archiveSaveFile = new File("./todo_archive.data");
	public final File alertSaveFile = new File("./todo_alerts.data");
	private int sWidth = 870, sHeight = 800;
	private JPanel panelHeadings, panelMain;
	private JMenuBar menuBar;
	private JMenu fileMenu, aboutMenu, optionsMenu, archiveMenu, helpMenu;
	private JMenuItem newToDoMenuItem, exitMenuItem, aboutMenuItem, updateDescriptionsMenuItem, scheduledToDoMenuItem;
	private JMenuItem deleteMenuItem, createBackupMenuItem, loadBackupMenuItem, archiveMenuItem;
	private JMenuItem filterSortMenuItem, helpMenuItem, createAlertsMenuItem, viewAlertsMenuItem;
	private JMenuItem createScheduledTodo, viewScheduledTodos;
	private List<ToDoHolder> toDoList = new ArrayList<ToDoHolder>();
	private List<ToDoHolder> toDoList_Filtered_Sorted = new ArrayList<ToDoHolder>();
	private JLabel lPriority, lDescription, lCreateDate, lLastModified;
	private Container container;
	private JScrollPane jsp;
	private AlertPane alertPane;
	private int adjustment = 0;
	private List<ToDoFilter> listFilters = new ArrayList<ToDoFilter>();
	private List<ToDoSort> listSorts = new ArrayList<ToDoSort>();
	private Timer alertTimer, autoSaveTimer;
	private AlertProcessor processor;
	
	public MainGui()
	{	
		//create container to place components in:
		container = getContentPane();
		container.setLayout(new FlowLayout());	//set flow layout
		
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();	//get screen resoloution
		
		if (d.height < 850) //if screen resolution too small to fit application
		{
			adjustment = 200;
		}
		sHeight -= adjustment;
		
		/*
		 * apply default sorts
		 */
		
		applyDefaultSorts();
		
		/*
		 * add menu bar
		 */
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		/*
		 * Do FileMenu
		 */
		
		fileMenu = new JMenu("File");
		
		newToDoMenuItem = new JMenuItem("New To Do...");
		createBackupMenuItem = new JMenuItem("Create a Backup File...");
		loadBackupMenuItem = new JMenuItem("Load from Backup File...");
		exitMenuItem = new JMenuItem("Exit");
		
		menuBar.add(fileMenu);
		
		fileMenu.add(newToDoMenuItem);
		fileMenu.add(createBackupMenuItem);
		fileMenu.add(loadBackupMenuItem);
		fileMenu.add(exitMenuItem);
		
		newToDoMenuItem.addActionListener(this);
		createBackupMenuItem.addActionListener(this);
		loadBackupMenuItem.addActionListener(this);
		exitMenuItem.addActionListener(this);
		
		/*
		 * Do Options Menu
		 */
		
		optionsMenu = new JMenu("Options");
		
		updateDescriptionsMenuItem = new JMenuItem("Update Descriptions...");
		deleteMenuItem = new JMenuItem("Delete To Dos...");
		filterSortMenuItem = new JMenuItem("Filtering & Sorting...");
		createAlertsMenuItem = new JMenuItem("Create Alerts...");
		viewAlertsMenuItem = new JMenuItem("View Alerts...");
		createScheduledTodo = new JMenuItem("Create Scheduled To Do...");
		viewScheduledTodos = new JMenuItem("View Scheduled To Dos...");
		
		menuBar.add(optionsMenu);
		
		optionsMenu.add(updateDescriptionsMenuItem);
		optionsMenu.add(deleteMenuItem);
		optionsMenu.add(filterSortMenuItem);
		optionsMenu.add(createAlertsMenuItem);
		optionsMenu.add(viewAlertsMenuItem);
		optionsMenu.add(createScheduledTodo);
		optionsMenu.add(viewScheduledTodos);
		
		updateDescriptionsMenuItem.addActionListener(this);
		deleteMenuItem.addActionListener(this);
		filterSortMenuItem.addActionListener(this);
		createAlertsMenuItem.addActionListener(this);
		viewAlertsMenuItem.addActionListener(this);
		createScheduledTodo.addActionListener(this);
		viewScheduledTodos.addActionListener(this);
		
		/*
		 * Do ArchiveMenu
		 */
		
		archiveMenu = new JMenu("Archives");
		
		archiveMenuItem = new JMenuItem("View Archived To Dos...");
		
		menuBar.add(archiveMenu);
		
		archiveMenu.add(archiveMenuItem);
		
		archiveMenuItem.addActionListener(this);	
		
		/*
		 * Do Help Menu
		 */
		
		helpMenu = new JMenu("Help");
		
		helpMenuItem = new JMenuItem("Help...");
		
		menuBar.add(helpMenu);
		
		helpMenu.add(helpMenuItem);
		
		helpMenuItem.addActionListener(this);
		
		/*
		 * Do AboutMenu
		 */
		
		aboutMenu = new JMenu("About");
		
		aboutMenuItem = new JMenuItem("About...");
		
		menuBar.add(aboutMenu);
		
		aboutMenu.add(aboutMenuItem);
		
		aboutMenuItem.addActionListener(this);		
		
		/*
		 * load from files
		 * 
		 * if any completed to dos are loaded in, put them in the archive and 
		 * remove them from active list
		 *
		 */
		
		loadInFiles();
		
		/*
		 * Do Panels
		 */

		alertPane = new AlertPane(sWidth - 20);
		panelHeadings = new JPanel();
		panelMain = createNewMainPanel();
		
		panelHeadings.setPreferredSize(new Dimension(850,25));
		//panelMain.setPreferredSize(mainPanelSize);
		
		panelHeadings.setBorder(new LineBorder(Color.BLACK));
		panelMain.setBorder(new LineBorder(Color.BLACK));
		
		/*
		 * Sort heading labels
		 */
				
		lPriority = new JLabel("Priority  ",JLabel.CENTER);
		lDescription = new JLabel("Description           ",JLabel.CENTER);
		lCreateDate = new JLabel("Created On   ",JLabel.CENTER);
		lLastModified = new JLabel("Last Modified  ",JLabel.CENTER);
		
		lPriority.setPreferredSize(new Dimension(90,15));
		lDescription.setPreferredSize(new Dimension(300,15));
		lCreateDate.setPreferredSize(new Dimension(90,15));
		lLastModified.setPreferredSize(new Dimension(90,15));
		
		panelHeadings.add(lPriority);
		panelHeadings.add(lDescription);
		panelHeadings.add(lCreateDate);
		panelHeadings.add(lLastModified);
		
		//dummy labels so that headings align properly
		JLabel lDummy1 = new JLabel(" ");
		lDummy1.setPreferredSize(new Dimension(60,15));
		panelHeadings.add(lDummy1);	
		JLabel lDummy2 = new JLabel(" ");
		lDummy2.setPreferredSize(new Dimension(130,15));
		panelHeadings.add(lDummy2);	
		
		
		/*
		 * add panels to container
		 */
		
		jsp = new JScrollPane(panelMain, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(850, calcJspHeight()));

		container.add(alertPane);
		container.add(panelHeadings);
		container.add(jsp);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//close when cross is clicked
		setLocation((d.width-sWidth)/2, (d.height-sHeight)/2);	//centre form
		
		setSize(sWidth,sHeight);	//set form size
		setVisible(true);//display screen
		
		processor = new AlertProcessor(alertSaveFile, this);
		alertTimer = new Timer();
		autoSaveTimer = new Timer();
		
		/*
		 * schedule alert checking every minute
		 */
		alertTimer.scheduleAtFixedRate(new AlertTimerTask(processor), 1000 * 5, 1000 * 60);

		/*
		 * scheduled todo checking every 90 seconds
		 */

		new Timer().scheduleAtFixedRate(new ScheduledToDoTimerTask(), 1000 * 90, 1000 * 50);

		/*
		 * Auto escalation timer
		 */

		new Timer().scheduleAtFixedRate(new AutoEscalationTimerTask(this), 1000 * 20, 1000 * 12);
		
		//autosave every XX Minutes
		
		int minutes = 30;
		
		autoSaveTimer.scheduleAtFixedRate(new AutoSaveTimerTask(this), 1000 * 60 * minutes, 1000 * 60 * minutes);
	}

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == exitMenuItem)
		{
			System.exit(0);
		}
		
		else if(e.getSource() == newToDoMenuItem)
		{
			new NewToDo(this);
		}
		
		else if(e.getSource() == createAlertsMenuItem)
		{
			String[] AlertOptions = {"Priority Alert", "Message Alert"};
			
			String selectedAlert = (String)JOptionPane.showInputDialog(this,"Which type of Alert do you wish to Schedule?", "Choose Alert",	JOptionPane.QUESTION_MESSAGE, null,	AlertOptions, AlertOptions[0]);
			
			if(selectedAlert != null)
			{
				if(selectedAlert.equals(AlertOptions[0])) //priority alert
				{
					new PriorityAlertConfigure(this, this);
				}
				else if(selectedAlert.equals(AlertOptions[1])) //message alert
				{
					new MessageAlertConfigure(this, this);
				}
			}
		}
		else if(e.getSource() == viewAlertsMenuItem)
		{
			new AlertViewer(this, getToDoItemList());
		}
		
		else if (e.getSource() == aboutMenuItem)
		{
			new AboutToDo();
		}
		
		else if (e.getSource() == updateDescriptionsMenuItem)
		{
			new DescriptionUpdate(toDoList_Filtered_Sorted, this);
		}
		
		else if (e.getSource() == deleteMenuItem)
		{
			new DeleteToDo(toDoList_Filtered_Sorted, this);
		}
		
		else if (e.getSource() == createBackupMenuItem)
		{
			File backupFile = getFileFromUser("Save", "Save Backup File");
			
			if (backupFile!=null)
			{
				createBackup(backupFile, true);
			}
			
		}
		
		else if (e.getSource() == loadBackupMenuItem)
		{
			File file = getFileFromUser("Load", "Load From Backup");
			
			if (file!=null)
			{
				int optionPicked = JOptionPane.showConfirmDialog(this, "Really load from backup file:\n" + file.toString() + "\n(this will overwrite your current To Do list and archives)?", "Confirm Load?", JOptionPane.YES_NO_OPTION);
				
				if(optionPicked == JOptionPane.YES_OPTION)
				{
					//remove all todo files in directory
					ToDoFileIO.destroyToDoFiles(saveFile, archiveSaveFile);
					
					//get file as string
					String strFile = ToDoFileIO.loadFile(file);
					
					//pass string to special backup-reading class
					ToDoBackupReader reader = new ToDoBackupReader(strFile);
					
					//request breakdown of files and associated text
					List<FileAndText> listOfFiles = reader.getFiles();
					
					//write files out to file system
					ToDoFileIO.writeFiles(listOfFiles);
					
					//tell class to load in files from file system again
					loadInFiles();
					
					//refresh display
					refreshToDoDisplay();
				}
			}
		}
		else if(e.getSource() == archiveMenuItem)
		{
			/*
			 * There seems to be a JVM problem that causes memory leaks
			 * when JFrames are closed - the only workaround is to force
			 * the application to minimise and restore itself. This is 
			 * forced when less than 33% of jvm memory is left. The
			 * reason this code is here is because the archive aggravates
			 * this JVM bug.
			 */
			long totalMemory = Runtime.getRuntime().totalMemory();
			long freeMemory =Runtime.getRuntime().freeMemory(); 
			
			if(freeMemory * 3 < totalMemory)
			{
				setState(JFrame.ICONIFIED);
				setState(JFrame.NORMAL);
			}
			
			/*
			 * check for other archives, give user option to pick them
			 */
			List<File> archiveList = ToDoFileIO.getExtraArchives();
			
			//put files in order by number
			ToDoUtilities.sortFiles(archiveList);
			
			Object[] archives = new Object[archiveList.size()+1];
			archives[0] = "1. Current Archive";
			
			Iterator i = archiveList.iterator();
			int index = 1;
						
			
			while(i.hasNext())
			{
				File f = (File)i.next();
				archives[index] = (index+1) + ". " + ToDoFileIO.getExtraInfo(f)[0] + " - " + ToDoFileIO.getExtraInfo(f)[1];
				index++;
			}
			
			String selectedArchive = (String)JOptionPane.showInputDialog(this,"Which archive do you wish to review?", "Choose Archive",	JOptionPane.QUESTION_MESSAGE, null,	archives, archives[0]);
			
			if(selectedArchive != null)
			{
				if(selectedArchive.equals(archives[0]))
				{
					/*
					 * open current archive
					 */
					
					new ArchivedToDoViewer(this, null);
				}
				else
				{
					boolean found = false;
					int count = 1;
					while(!found)
					{
						/*
						 * create new archive form showing selected archive
						 */
						if(selectedArchive.equals(archives[count]))
						{
							found = true;
							
							new ArchivedToDoViewer(this, archiveList.get(count -1));
						}
						
						count++;
					}
				}
			}
			
			
		}
		else if(e.getSource() == filterSortMenuItem)
		{
			new FilterSortMenu(this);
		}
		else if(e.getSource() == helpMenuItem)
		{
			new HelpForm();
		} else if(e.getSource() == createScheduledTodo) {
			new ScheduledToDoGui();
		} else if(e.getSource() == viewScheduledTodos) {
			new ScheduledToDoViewGui();
		}
		else
		{
		
			/*
			 * need to iterate displayed todos, identify if one of the buttons 
			 * associated with active todos has been pressed
			 */
			
			Iterator i = toDoList_Filtered_Sorted.iterator();
			
			whileLoop:
			while(i.hasNext())
			{
				ToDoHolder holder = (ToDoHolder)i.next();
				
				if (e.getSource() == holder.getBPriority()) //change of priority for this todo
				{
					new PriorityChange(holder, this);
					break whileLoop;
				}
				
				if (e.getSource() == holder.getBLog())
				{
					new LogToDo(holder, this);
					break whileLoop;
				}
				
				if (e.getSource() == holder.getBCompleted())
				{
					new CompleteToDo(holder, this, this);
					break whileLoop;
				}
	
			}
		}
	}
	
	public void refreshToDoDisplay()
	{	
		/*
		 * Save position (fix to bug where position resets to top after refresh)
		 */
		final int pos = jsp.getVerticalScrollBar().getValue();
		System.out.println(pos);
		/*
		 * allocate IDs where necessary
		 */
		
		List<ToDoItem> itemList = ToDoUtilities.convertHolderListToItemList(toDoList);
		
		ToDoUtilities.allocateIds(itemList);
		
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
			
			if (c == jsp)
			{
				container.remove(index);
			}
			index++;
		}
		
		panelMain = createNewMainPanel();
		
		jsp = new JScrollPane(panelMain, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(850,calcJspHeight()));
		
		Runnable doScroll = new Runnable() {
            public void run() {
           	 jsp.getVerticalScrollBar().setValue(pos);
            }
        };

        SwingUtilities.invokeLater(doScroll);

		panelMain.revalidate();
		container.add(jsp);
		container.validate();
		validate();
		repaint();
		
		/*
		 * save to disk automatically
		 */
		ToDoFileIO.saveToDosAsFile(saveFile, ToDoUtilities.convertHolderListToItemList(toDoList));
	}
	
	public JPanel createNewMainPanel()
	{	
		
		/*
		 * create panel
		 */
		
		JPanel newMainPanel = new JPanel();
		
		SpringLayout sLayout = new SpringLayout();
		
		//newMainPanel.setPreferredSize(mainPanelSize);
		newMainPanel.setBorder(new LineBorder(Color.BLACK));
		newMainPanel.setLayout(sLayout);

		
		/*
		 * apply filters & then apply sorts
		 */
		
		//need to convert to item list, afterwards convert back again
		List<ToDoItem> sortedListAsItems = ToDoUtilities.convertHolderListToItemList(toDoList);
		
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
		
		toDoList_Filtered_Sorted = ToDoUtilities.convertItemListToHolderList(sortedListAsItems);	
		
		//get iterator
		Iterator i = toDoList_Filtered_Sorted.iterator();
		
		boolean firstIteration = true;
		JPanel previousRowPanel = null;
		
		int panelHeight = 0;
		
		while(i.hasNext())
		{
			ToDoHolder holder = (ToDoHolder)i.next();
			
			JPanel newRowPanel = holder.getAsJPanel();

			if(firstIteration)
			{
				sLayout.putConstraint(SpringLayout.NORTH, newMainPanel, 3, SpringLayout.NORTH, newRowPanel);
				firstIteration = false;
//				need to calculate required height of panel
				panelHeight += 38;
			}
			else
			{
				sLayout.putConstraint(SpringLayout.NORTH, newRowPanel, 3, SpringLayout.SOUTH, previousRowPanel);
//				need to calculate required height of panel
				panelHeight += 33;
			}
			
			
			
			previousRowPanel = newRowPanel;
			
			newMainPanel.add(newRowPanel);
			
			//add actionlisteners to buttons for this todo
			holder.getBPriority().addActionListener(this);
			holder.getBLog().addActionListener(this);
			holder.getBCompleted().addActionListener(this);
			holder.addToDoObserver(this);
		}
				
		newMainPanel.setPreferredSize(new Dimension(830,panelHeight));

		setTitle("To Do List - " + toDoList_Filtered_Sorted.size() + " of "+ toDoList.size() + " active to do items");
		return newMainPanel;
	}
	
	public void removeActionListeners()
	{
		Iterator i = toDoList.iterator();
		
		while (i.hasNext() == true)
		{
			ToDoHolder holder = (ToDoHolder)i.next();
			holder.getBPriority().removeActionListener(this);
			holder.getBLog().removeActionListener(this);
			holder.getBCompleted().removeActionListener(this);
			holder.removeToDoObserver(this);
		}
	}
	
	public void addToDo(ToDoHolder holder)
	{
		toDoList.add(holder);
		refreshToDoDisplay();
	}
	
	/**
	 * archives any completed todos from list, overwrite parameter refers to 
	 * whether or not method appends to existing archive or overwrites it
	 * completely
	 * @param list
	 * @param overwrite
	 */
	public void archiveCompletedToDos(List<ToDoHolder> list, boolean overwrite)
	{
		Iterator i = list.iterator();
		
		List<ToDoHolder> oldToDos = new ArrayList<ToDoHolder>();
		
		/*
		 * first loop - takes a copy of oldToDos
		 */
		while (i.hasNext())
		{
			ToDoHolder thisHolder = (ToDoHolder)i.next();
			if(thisHolder.isCompleted())
			{
				oldToDos.add(thisHolder);
			}
		}
		
		/*
		 * only continue if old todos found
		 */
		if (oldToDos.size() > 0)
		{
		
			/*
			 * second loop - remove old Todos
			 */
			i = list.iterator();
			
			int index=0;
			
			while(index < list.size())
			{
				ToDoHolder holder = list.get(index);
				
				if(holder.isCompleted())
				{
					list.remove(index);
					/*
					 * dont need to increment index since current position has
					 * been removed
					 */
				}
				else
				{
					index++;
				}
			}
			
			/*
			 * if not overwriting existing archive, append to existing archive only
			 */
			if(!overwrite)
			{
				Iterator iOld = oldToDos.iterator();
				
				while(iOld.hasNext())
				{
					ToDoItem item = ((ToDoHolder)iOld.next()).getToDoItem();
					ToDoFileIO.appendToDoToFile(archiveSaveFile, item);
				}
			}
			else //overwrite old archive
			{
				ToDoFileIO.saveToDosAsFile(archiveSaveFile, ToDoUtilities.convertHolderListToItemList(oldToDos));
			}
			
			ToDoFileIO.saveToDosAsFile(saveFile, ToDoUtilities.convertHolderListToItemList(toDoList));
			
		}		
		//update window title
		setTitle("To Do List - " + toDoList.size() + " active to do items");
	}

	public List<ToDoHolder> getToDoList() {
		return toDoList;
	}
	
	/**
	 * method that creates a filechooser and gets a file from the user
	 * @return
	 */
	public File getFileFromUser(String buttonText, String dialogTitle)
	{
		JFileChooser jfcSave = new JFileChooser();	//create new file chooser
		jfcSave.setFileFilter(new dataFileFilter());
		jfcSave.setApproveButtonText(buttonText);	//set confirm button text
		jfcSave.setDialogTitle(dialogTitle);	//dialog heading
		int returnVal = jfcSave.showOpenDialog(new JFrame());	//get file chosen

		File selectedFile = null;	//declare file

		if(returnVal == JFileChooser.APPROVE_OPTION)	//if file chosen is ok
  		{
    			selectedFile = jfcSave.getSelectedFile();	//store chosen file
		}

	    return selectedFile;
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

	/**
	 * applies the default sorts on this list
	 */
	private void applyDefaultSorts()
	{
		DescriptionSort sort1 = new DescriptionSort(DescriptionSort.SORT_DESCENDING);
		PrioritySort sort2 = new PrioritySort(PrioritySort.SORT_DESCENDING);
		listSorts.add(sort1);
		listSorts.add(sort2);
	}

	public void deleteToDoItem(ToDoItem item) 
	{
		int index = ToDoUtilities.getIndexOfHolderInList(new ToDoHolder(item), toDoList);
		
		toDoList.remove(index);
		
		refreshToDoDisplay();	
		
	}
	
	public List<ToDoHolder> getFilteredSortedToDoList() 
	{
		return toDoList_Filtered_Sorted;
		
	}
	
	private void loadInFiles()
	{
		toDoList = ToDoFileIO.loadToDosAsHolderList(saveFile);
		archiveCompletedToDos(toDoList, false);
		setTitle("To Do List - " + toDoList.size() + " active to do items");
		
		/*
		 * allocate ids, if changes made then save file
		 */
		
		List<ToDoItem> itemList = ToDoUtilities.convertHolderListToItemList(toDoList);
		
		if(ToDoUtilities.allocateIds(itemList))
		{
			ToDoFileIO.saveToDosAsFile(saveFile, itemList);
		}
	}

	public void addAlert(Alert a) 
	{
		ToDoFileIO.appendAlertToFile(alertSaveFile, a);
		
		//since you are adding a new alert, need to notify the alert
		//processor so that it can pick it up
		processor.refreshList();
		
	}

	public List<ToDoItem> getToDoItemList() 
	{
		
		return ToDoUtilities.convertHolderListToItemList(toDoList);
	}

	public List<Alert> getListAlerts()
	{
		
		return processor.getListAlerts();
	}

	public void removeAlert(Alert a) 
	{
		processor.removeAlert(a);	
	}

	public void removeAllAlertsForID(int id) 
	{
		processor.removeAllAlertsForID(id);	
	}
	
	public void createBackup(File backupFile, boolean showMessage)
	{
		/*
		 * amalgamate all files into one big one
		 */
		
		/*
		 * this big if statement returns true if backUpFile does not 
		 * end in a .data extension
		 */
		if (backupFile.toString().length() < 6 || !backupFile.toString().substring(backupFile.toString().length() - 5, backupFile.toString().length()).equalsIgnoreCase(".data"))
		{
			backupFile = new File(backupFile + ".data");
		}
			
		List<File> filesToSave = new ArrayList<File>();
		
		filesToSave.add(saveFile);
		filesToSave.add(archiveSaveFile);
		
		List<File> otherArchives = ToDoFileIO.getExtraArchives();
		
		Iterator<File> i = otherArchives.iterator();
		
		while (i.hasNext())
		{
			filesToSave.add(i.next());
			
		}
		
		List<File> iniFiles = ToDoFileIO.getIniFiles();
		
		i = iniFiles.iterator();
		
		while (i.hasNext())
		{
			filesToSave.add(i.next());
		}
		ToDoFileIO.amalgamateFiles(backupFile, filesToSave);
		
		if(showMessage)
		{
			JOptionPane.showMessageDialog(null, "Backup saved to '" + backupFile + "'", "Backup Complete", JOptionPane.INFORMATION_MESSAGE);
		}
		
	}

	@Override
	public void onEvent(ToDoEvent e) {

		if(e.getType() == ToDoEvent.Type.CONFIGURE_AUTO_ESCALATION) {
			AutoEscalationCommand command = (AutoEscalationCommand) e.getEventInfo();
			ToDoSchedule schedule = new ToDoSchedule(
					Calendar.getInstance(),
					command.getDayFrequency(),
					command.getTargetPriority(),
					command.isNotify()
			);

			e.getToDoItem().setSchedule(schedule);
			refreshToDoDisplay();
		} else if(e.getType() == ToDoEvent.Type.CANCEL_AUTO_ESCALATION) {
			e.getToDoItem().setSchedule(null);
			refreshToDoDisplay();
		}
	}

	private int calcJspHeight() {
		int jspHeight = 690 - adjustment;
		jspHeight -= alertPane.getHeight();
		if(alertPane.getHeight() > 0) {
			jspHeight -= 5; //margin
		}

		System.out.println("JSP Height is " + jspHeight);
		return jspHeight;
	}

	public void addInfoMessage(AlertPane.AlertType type, String message) {
		alertPane.addAlert(type, message);
	}
}
