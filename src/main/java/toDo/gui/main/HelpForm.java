//© Copyright Jonathan Devine 2007
//No code may be copied or reproduced without my express permission
//failure to comply will result in legal action

package toDo.gui.main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import toDo.data.*;

public class HelpForm extends JFrame implements ActionListener
{
	private static final long serialVersionUID = -1063347708374687469L;
	private int sWidth = 650, sHeight = 600;
	private JPanel headerPanel, topicPanel, textPanel, buttonPanel;
	private JPanel mainTopicPanel, subTopicPanel;
	private JLabel lHeader;
	private static DefaultListModel dlmSub;
	private static JList listMain, listSub;
	//array to contain main help topics
	private static String[] arrMainTopics;
	//array containing HelpEntry objects
	private static HelpEntry[][] arrSubTopics;
	private static JTextArea textArea;
	private JButton bClose;
	
	public HelpForm()
	{
		super("Help Menu");
		//set the help information before rendering
		setHelpInformation();
		
		Container container = getContentPane();
	    container.setLayout(new FlowLayout());	//set flow layout
	    
	    headerPanel = new JPanel();
	    topicPanel = new JPanel();
	    textPanel = new JPanel();
	    buttonPanel = new JPanel();
	    
	    mainTopicPanel = new JPanel();
	    subTopicPanel = new JPanel();
	    
	    headerPanel.setPreferredSize(new Dimension(630,50));
	    topicPanel.setPreferredSize(new Dimension(630,180));
	    textPanel.setPreferredSize(new Dimension(630,260));
	    buttonPanel.setPreferredSize(new Dimension(630,40));
	    
	    mainTopicPanel.setPreferredSize(new Dimension(300,160));
	    subTopicPanel.setPreferredSize(new Dimension(300,160));
	    
	    headerPanel.setBorder(new LineBorder(Color.BLACK));
	    topicPanel.setBorder(new LineBorder(Color.BLACK));
	    textPanel.setBorder(new LineBorder(Color.BLACK));
	    buttonPanel.setBorder(new LineBorder(Color.BLACK));
	    
	    mainTopicPanel.setBorder(new LineBorder(Color.BLACK));
	    subTopicPanel.setBorder(new LineBorder(Color.BLACK));
	    
	    //headerPanel components
	    lHeader = new JLabel("Help Menu");
	    
	    lHeader.setFont(new Font("Comic Sans MS", Font.BOLD, 24));
	    
	    lHeader.setForeground(Color.BLACK);
	    
	    headerPanel.add(lHeader);
	    
	    //mainTopicPanel components (subpanel of topicPanel)
	    listMain = new JList(arrMainTopics);
	    
	    listMain.setFixedCellWidth(280);
		listMain.setVisibleRowCount(8);
		
		listMain.addListSelectionListener(new MainValueReporter());
	
	    mainTopicPanel.add(new JScrollPane(listMain));
	    
	    //subTopicPanel components (subpanel of topicPanel)
	    dlmSub = new DefaultListModel();
	    dlmSub.addElement(" "); //blank record for rendering purposes
	    
	    listSub = new JList(dlmSub);
	    
	    listSub.setFixedCellWidth(280);
		listSub.setVisibleRowCount(8);
		
		listSub.addListSelectionListener(new SubValueReporter());
		
		subTopicPanel.add(new JScrollPane(listSub));
	    
	    //textPanel components
		textArea = new JTextArea("Help text appears here");
		
		textArea.setEditable(false);
		
		textArea.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		
		//text area needs to be scrollable
		JScrollPane textScrollPane = new JScrollPane(textArea);
		textScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		textScrollPane.setPreferredSize(new Dimension(610,245));
		
		textPanel.add(textScrollPane);
		
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		
	    //buttonPanel components
		bClose = new JButton("Close");
		
		bClose.setPreferredSize(new Dimension(160,25));
		
		bClose.addActionListener(this);
		
		buttonPanel.add(bClose);
	    
	    //add panels to form
	    topicPanel.add(mainTopicPanel);
	    topicPanel.add(subTopicPanel);
	    
	    container.add(headerPanel);
	    container.add(topicPanel);
	    container.add(textPanel);
	    container.add(buttonPanel);
	    
	    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();	//get screen resoloution
	    setLocation((d.width-sWidth)/2, (d.height-sHeight)/2);
	    
	    setSize(sWidth,sHeight);	//set form size
		setVisible(true);		//make form visible	
	}
	
	/**
	 * method runs at start of constructor to create the arrays that contain the
	 * text information displayed by the help menu, this includes the main topic,
	 * the sub topic and the text associated with the two previous items
	 */
	private static void setHelpInformation()
	{
		//variables will be re-used, declared once here:
		String subTopic;
		String subTopicText;
		HelpEntry helpEntry;
		
		/*two arrays store the help information, one contains main topics only,
		 * the other is a multidimensional array of HelpEntry objects. HelpEntry
		 * objects contain two stings, a subtopic and a help text object. the two
		 * arrays must be the same size, the data in the main topic array is associated
		 * with the data in the helpentry array. 
		 * 
		 * i.e:
		 * arrSubTopics[x][] contains all the subtopics and associated text for the
		 * main topic contains in arrMainTopics[x]
		 */
		
		//this variable is used to decide the size of the arrays containing todo
		//help information:
		int numberOfMainTopics = 8;
		
		 arrMainTopics = new String[numberOfMainTopics];
		 arrSubTopics = new HelpEntry[numberOfMainTopics][];
		 
		 //Main topic 1 is 'Getting started'
		 arrMainTopics[0] = "Getting Started";
		 
		 //need to declare size (ie number of subtopics) of helpentry array at index 0:
		 arrSubTopics[0] = new HelpEntry[5];
		 
		 //subtopic of 'Getting started':
		 subTopic = "Creating a New To Do";
		 subTopicText = 
			 "To create a new To Do item, click the 'File' menu at the top " +
			 "of the To Do list, then select 'New To Do...'. This will bring " +
			 "up a small form with the heading 'Create a New To Do'. On this " +
			 "form you need to enter a description, set the priority of the " +
			 "item and optionally enter some starting comments. Once you have " +
			 "done this, click 'Create this To Do' and your To Do will appear " +
			 "on the To Do list and the Create form will close.";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[0][0] = helpEntry;
		 
		 //subtopic of 'Getting started':
		 subTopic = "The To Do Display";
		 subTopicText = 
			 "As you would expect, the To Do list (the main form that opens " +
			 "when you start the application) shows a list of all your active " +
			 "To Do items. On the list, you will see information relating to " +
			 "priority, description, create date & time and the last modified " +
			 "date & time of each To Do. Additionally, there are buttons for " +
			 "each To Do labelled 'Log' and 'Completed?'." +
			 "\n\n" +
			 "Of the information displayed, 'Description', 'Created On' and " +
			 "'Last Modified' are read-only. Description can be modified " +
			 "indirectly however, via the 'Options' -> 'Update Descriptions...' " +
			 "form. On each To Do there are three buttons, one representing " +
			 "priority (this will show either 'Urgent', 'High', 'Medium' or " +
			 "'Low'), one labelled 'Log' and one labelled 'Completed'. The " +
			 "buttons on each To Do are used only to complete operations related " +
			 "to that To Do. ";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[0][1] = helpEntry;
		 
		 //subtopic of 'Getting started':
		 subTopic = "Changing the Priority of To Dos";
		 subTopicText = 
			 "As you progress your To Do items, you are likely to find that " +
			 "the priority of some To Dos change, therefore it is useful to " +
			 "represent this on your To Do list. To change the priority of a " +
			 "To Do item, click the priority button (labelled either 'Urgent', " +
			 "'High', 'Medium' or 'Low') for that To Do. You will then get a " +
			 "menu asking for a new priority. Click the priority you would like " +
			 "to change the item to; at this point the menu will close and your " +
			 "To Do list will update.";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[0][2] = helpEntry;
		 
		 //subtopic of 'Getting started':
		 subTopic = "Updating the Log";
		 subTopicText = 
			 "Each To Do item keeps a written log of your activities relating " +
			 "to that task. To view the log for any To Do, click the 'Log' " +
			 "button for that To Do. The log screen will then appear. You will " +
			 "see two text boxes; the one at the top shows a read-only display " +
			 "of the current log and the bottom one shows a blank text area that " +
			 "you can type into. " +
			 "\n\n" +
			 "The written log will have two types of comments in it; either " +
			 "'COMMENT' or 'AUDIT'. Audit text is added by the application " +
			 "itself, this occurs on events such as changing the priority of " +
			 "a To Do, creating a To Do, closing a To Do etc. Comment text is " +
			 "added by you, the user. This allows you to keep a record of your " +
			 "activities and actions relating to the To Do. To add a comment, " +
			 "type your comments into the bottom text box of the log screen and " +
			 "click 'Append'. You will see your comment appended the main log " +
			 "text.";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[0][3] = helpEntry;
		 
		 //subtopic of 'Getting started':
		 subTopic = "Completing a To Do";
		 subTopicText = 
			 "Obviously once you have completed a To Do, you will want to " +
			 "remove it from your list. To mark a To Do complete and remove " +
			 "it from the main To Do List, click the 'Complete?' button for " +
			 "that To Do. An input screen will open where you can input any " +
			 "comments (this is optional) relating to the closure of a To Do. " +
			 "If you are sure you want to close the To Do item, click 'Mark " +
			 "Complete'. Your To Do will no longer be on the main To Do list, " +
			 "however a record of it will be kept in the archives. For more " +
			 "information on the archives, please see the 'Managing the Archives' " +
			 "help topic.";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[0][4] = helpEntry;
		 
		 
		 
		 //Main topic 2 is 'Backing up your To Do List'
		 arrMainTopics[1] = "Backing up your To Do List";
		 
		 //need to declare size (ie number of subtopics) of helpentry array at index 1:
		 arrSubTopics[1] = new HelpEntry[2];
		 
		 //subtopic of 'Backing up your To Do List':
		 subTopic = "Creating a Backup of Your To Do List";
		 subTopicText = 
			 	"It is advisable to take a backup of your To Do List regularly " +
			 	"(once a day recommended). If your hard disk crashes, you " +
			 	"delete the data files by mistake or some other problem causes " +
			 	"you to lose your data, a backup will allow you to get all " +
			 	"your data back, up to the point at which the backup was taken." +
			 	"\n\n" +
			 	"A backup will take a copy of all your active To Dos as well " +
			 	"as all the To Dos stored in your archives." +
			 	"\n\n" +
			 	"To create a backup, select the 'File' menu and then " +
			 	"select 'Create a Backup File...’. You will then get a file " +
			 	"browser menu to browse your hard disk and select where to " +
			 	"save your backup file. When you have selected a directory " +
			 	"and name for your backup, click 'Save'. You should then get " +
			 	"a confirmation that your file was saved." +
			 	"\n\n" +
			 	"Note: Backup files expect to be saved as '.data' files. If you " +
			 	"specify a filename without the '.data' extension, it will be " +
			 	"appended onto the filename automatically.";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[1][0] = helpEntry;
		 
		 //subtopic of 'Backing up your To Do List':
		 subTopic = "Restoring a Backup of Your To Do List";
		 subTopicText = 
			 	"If you lose your data or for any reason wish to return your" +
		 		" to do list to a previous backup you will need to restore from" +
		 		" a backup file. To restore from a backup file, click the " +
		 		"'File' -> 'Load from Backup File...' option." +
		 		"\n\n" +
		 		"You will then need to browse your hard disk to find the backup " +
		 		"file (it should have a '.data' extension). Once you have found " +
		 		"and selected the backup file, click 'Load'. You will get a " +
		 		"message warning you that loading the backup will overwrite " +
		 		"all existing data, if you are happy with this click 'Yes' on " +
		 		"the prompt. Assuming no errors on loading the backup, your To " +
		 		"Do List and archives should now contain the data that was saved " +
		 		"in the backup." +
		 		"\n\n" +
		 		"Note: It is important to remember that loading a backup will " +
		 		"wipe all current data that you have stored - therefore only " +
		 		"restore a backup if you are willing to lose that data."; 
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[1][1] = helpEntry;
		 
		 //Main topic 3 is 'Filtering and Sorting'
		 arrMainTopics[2] = "Filtering and Sorting";
		 
		 //need to declare size (ie number of subtopics) of helpentry array at index 1:
		 arrSubTopics[2] = new HelpEntry[3];
		 
		 //subtopic of 'Filtering and Sorting'
		 subTopic = "Filters and Sorts";
		 subTopicText =
			 "When you have many To Dos, it can be helpful to arrange the To " +
			 "Dos in an order relevant to your working style, or to filter " +
			 "out To Dos that you are not currently interested in. For this " +
			 "reason, custom filters and sorts can be applied to your To Do " +
			 "List and archive to allow you to navigate the information more " +
			 "easily. This is particularly useful when you have a large archive, " +
			 "as you can easily sort and filter the archived To Dos so that you " +
			 "can find the ones you want quickly." +
			 "\n\n" +
			 "In order to start creating filters and sorts, select the " +
			 "'Options' -> 'Filtering and Sorting' menu item from either your " +
			 "To Do List or the archive (the filters and sorts for these two " +
			 "forms are separate). This will open the Filtering and Sorting " +
			 "Menu. To see details of how to create filters and sorts, please " +
			 "read the rest of this section."; 
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[2][0] = helpEntry;
		 
		 //subtopic of 'Filtering and Sorting'
		 subTopic = "Filters";
		 subTopicText =
			 "A filter is a specification of which To Dos you want to see. " +
			 "Applying a filter correctly will hide To Dos you are not " +
			 "currently interested in and display only the ones you want to " +
			 "work with." +
			 "\n\n" +
			 "On the Filtering/Sorting Menu, you will notice a list for " +
			 "filters on the left hand side. By default this list is empty " +
			 "for both the To Do List and the archive." +
			 "\n\n" +
			 "There are 3 types of filter available. These are:" +
			 "\n\n" +
			 "*Filter by Date (either Create Date or Last Modified Date)\n" +
			 "*Filter by Priority\n" +
			 "*Filter by Description" +
			 "\n\n" +
			 "Date filters allow you to filter either based on dates later " +
			 "than a time you specify, dates earlier than a time you specify " +
			 "or dates between two dates that you specify. To create a date " +
			 "filter, click the 'Add Filter' button on the Filter/Sort Menu. " +
			 "You will then get a choice of date or priority filters, choose " +
			 "'Filter by Date' here." +
			 "\n\n" +
			 "You will then see a new form for specifying the parameters of " +
			 "the date filter. Firstly you must choose which date to filter " +
			 "by, either choose create date or last modified date from the " +
			 "drop down list. If you only wish to filter To Dos before or " +
			 "after an appropriate date, enter a single date (you need to " +
			 "specify all fields for DD, MM, YY, hh, mm) in the appropriate " +
			 "date input. To search for To Dos between a pair of dates, " +
			 "you must enter a date in both inputs. You must enter complete " +
			 "dates only, incomplete dates will cause an error and the filter " +
			 "will not be created." +
			 "\n\n" +
			 "Once you are happy with the date filter parameters, click " +
			 "'Confirm Filter'. If your filter is valid, the form will " +
			 "close and you should see a new filter in the list of filters " +
			 "on the Filter/Sort Menu. The text in the list describes " +
			 "which parameters must be true in order for a To Do to be displayed." +
			 "\n\n" +
			 "Priority filters allow you to filter based on the priority of " +
			 "a To Do. In order to create a priority filter, click the 'Add " +
			 "Filter...' button on the Filter/Sort Menu, then choose 'Filter " +
			 "by Priority' on the menu that appears." +
			 "\n\n" +
			 "A form will appear asking which priorities you wish to filter. " +
			 "Make sure the priorities you want to remain visible are ticked " +
			 "and the ones that you wish to hide are unticked. When you are " +
			 "happy with your selection, click confirm." +
			 "\n\n" +
			 "Description filters act as a search mechanism. You specify " +
			 "keywords (seperated with a space) which act as search terms " +
			 "for your To Dos. You can specify case sensitivity and whether " +
			 "you want to find To Dos with a minimum of one match against " +
			 "you keywords or To Dos that match all your keywords. This " +
			 "functionality is useful for finding To Dos in your archive " +
			 "when you have a large number of archived To Dos." +
			 "\n\n" +
			 "You should see a new filter appear in the list on the " +
			 "Filter/Sort Menu, it will say which priorities are visible " +
			 "when the filter is applied." +
			 "\n\n" +
			 "To remove filters from the list, select one or more filters " +
			 "and click 'Remove Filter'." +
			 "\n\n" +
			 "You will notice that when some of your To Dos are filtered, " +
			 "the To Do List/Archive heading changes. For example, if you " +
			 "have 10 open To Dos, and you apply a filter so that 5 are " +
			 "hidden, the heading would change to 'To Do List - 5 of 10 " +
			 "active to do items'. This implies that you have 10 open to " +
			 "dos, however only 5 are being displayed and the rest are hidden." +
			 "\n\n" +
			 "To apply your filters (or even the removal of filters) to your " +
			 "To Do list or archive, click the 'Apply' button on the " +
			 "Filter/Sort menu."; 
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[2][1] = helpEntry;
		 
		 //subtopic of 'Filtering and Sorting'
		 subTopic = "Sorts";
		 subTopicText =
			 "A sort is a specification of the order in which to display " +
			 "To Do items. You will notice that by default the To Do List " +
			 "has two sorts (sort by priority and then sort by description), " +
			 "while the archive has one (sort by description). You can apply " +
			 "your own sorts to put your To Dos into an order " +
			 "suitable for your own purposes." +
			 "\n\n" +
			 "In order to create a sort, click the 'Add Sort...' button on " +
			 "the Filter/Sort Menu. You will then get a menu asking what " +
			 "you want to sort by; date, priority or description." +
			 "\n\n" +
			 "If you choose to sort by date you will be asked to choose " +
			 "either to sort by create date or last modified date." +
			 "\n\n" +
			 "Once you have chosen a field to sort by, you will be asked " +
			 "to choose to sort either ascending or descending. Once you " +
			 "have made this choice and confirmed it your sort will be created." +
			 "\n\n" +
			 "In order to perform more complex sorts, you may perform more than" +
			 " one sort at a time. As per the default for the To Do List, the " +
			 "list is sorted by priority and then by description. This means " +
			 "that the primary sort is by priority, then where priorities are " +
			 "equal the To Dos are sorted by description, this is the secondary " +
			 "sort." +
			 "\n\n" +
			 "To create and apply more than one sort, you need to decide the " +
			 "priority of the sorts. Once you create the sorts, select the sorts " +
			 "in the list and use the up and down arrows to position the sort in " +
			 "the list according to priority, with the highest priority at the " +
			 "top." +
			 "\n\n" +
			 "To apply your sorts (or even the removal of sorts) to your To Do " +
			 "list or archive, click the 'Apply' button on the Filter/Sort menu.";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[2][2] = helpEntry;
		 
		 //	Main topic 4 is 'Managing the Archive'
		 arrMainTopics[3] = "Managing the Archive";
		 
		 //need to declare size (ie number of subtopics) of helpentry array at index 1:
		 arrSubTopics[3] = new HelpEntry[3];
		 
		 //subtopic of 'Managing the Archive'
		 subTopic = "The To Do Archives";
		 subTopicText =
			 "The To Do archives contain information about all the To Dos you " +
			 "have ever completed. They allow you to go back and refer to your " +
			 "previous actions or other information stored in relation to " +
			 "previous tasks that could be useful to you at the present time." +
			 "\n\n" +
			 "The archives are a similar format to the To Do List, they shows " +
			 "priority, description, create date, last modified date and have " +
			 "a button to access the log (although the log is read-only). You " +
			 "cannot, however, modify any of the To Dos in the archives. The " +
			 "only options available in the archives are to view To Dos, delete " +
			 "To Dos and filter/sort To Dos. This is because the archives are " +
			 "meant for reference, not for actively managing To Dos.";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[3][0] = helpEntry;
		 
		 //subtopic of 'Managing the Archive'
		 subTopic = "Managing the Archives";
		 subTopicText =
			 "When you start using the To Do application you will only have " +
			 "a single archive. This is known as the 'current archive'. The " +
			 "current archive is where To Dos completed from the main To Do " +
			 "List will get stored. Eventually you will find the archive gets " +
			 "very large, as it gets larger you will notice it runs slower. " +
			 "For this reason, it is possible to isolate your archive into " +
			 "separate chunks so that you can access them without the " +
			 "application grinding to a halt." +
			 "\n\n" +
			 "You will automatically get prompted to isolate the To Dos in " +
			 "your current archive once you have over 250 To Dos in it. When " +
			 "you isolate your current archive, a new file is created and " +
			 "stored separately, this file contains all the information in " +
			 "your current archive. Once this file is created, your current " +
			 "archive is cleared down. Newly completed To Dos from this point " +
			 "will be sent to the now empty current archive. You can still " +
			 "access the isolated archive by going to 'Archives' -> 'View " +
			 "Archived To Dos' on the menu of the main To Do list and choosing " +
			 "the isolated archive from the drop-down list given. The drop " +
			 "down list will show you your current archive and all your " +
			 "isolated archives - the isolated archives are represented by " +
			 "the earliest and latest closing date of the To Dos stored in " +
			 "them.";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[3][1] = helpEntry;
		 
		 //subtopic of 'Managing the Archive'
		 subTopic = "Isolating your Archives";
		 subTopicText =
			 "You will get prompted to isolate your current archive when it " +
			 "has over 250 To Dos in it - you can however manually force " +
			 "an isolation of the current archive at any time. To do this, " +
			 "click 'Options'->'Send to New Archive...' on the archive window." +
			 "\n\n" +
			 "The feature to isolate archives was added to improve " +
			 "performance - therefore if your current archive is running " +
			 "slowly it is a good idea to isolate it before it gets any " +
			 "slower.";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[3][2] = helpEntry;
		 
		 //Main topic 5 is 'General Advice'
		 arrMainTopics[4] = "General Advice";
		 
		 //need to declare size (ie number of subtopics) of helpentry array at index 4:
		 arrSubTopics[4] = new HelpEntry[3];
		 
		 //subtopic of 'General Advice':
		 subTopic = "Take Regular Backups!";
		 subTopicText = 
			 "Take a backup at least once a day - if you don’t take any " +
			 "backups and lose all your data it is your own fault!";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[4][0] = helpEntry;
		 
		 //subtopic of 'General Advice':
		 subTopic = "Don't mess with the data files!";
		 subTopicText = 
			 "The To Do application stores at least two files - 'todo.data', " +
			 "'todo_archive.data' & possibly other files with the extensions " +
			 "'.data' or '.ini'. Do not attempt to edit these files! " +
			 "These files must remain in the same directory on your computer " +
			 "as the main 'todo.jar' file." +
			 "\n\n" +
			 "If you really feel the need to modify the data files please " +
			 "bear the following in mind:" +
			 "\n\n" +
			 "*Take a back-up first.\n" +
			 "*The files are stored in a form of XML\n" +
			 "*The algorithm to read in the XML isn't that " +
			 "clever, with the exception of the <todoitem> and " +
			 "<log> tags it will get confused if the open and " +
			 "close tags are not on the same line.\n" +
			 "*The algorithm may not understand nested tags on the same line.";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[4][1] = helpEntry;
		 
		 //subtopic of 'General Advice':
		 subTopic = "Put a Link on the Quick Start Menu";
		 subTopicText = 
			 "It would be sensible to put the main 'todo.jar' file in a " +
			 "directory by itself (not on the desktop). This way you " +
			 "will be unlikely to delete your data by mistake! It is a " +
			 "good idea to put a shortcut to 'todo.jar' either on the " +
			 "desktop, or on the quick launch toolbar (recommended - this " +
			 "is the toolbar by the 'Start' button on the Windows operating " +
			 "system).";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[4][2] = helpEntry;
		 
		 //Main topic 6 is 'Troubleshooting'
		 arrMainTopics[5] = "Troubleshooting";
		 
		 //need to declare size (ie number of subtopics) of helpentry array at index 1:
		 arrSubTopics[5] = new HelpEntry[3];
		 
		 //subtopic of 'Troubleshooting':
		 subTopic = "Application Running Slow";
		 subTopicText = 
			 "This could just be because you have a slow computer however you " +
			 "can try the following steps to try to minimise memory usage " +
			 "by the to do application : " +
			 "\n\n" +
			 "*Don't keep windows open that you dont need (particularly the " +
			 "archive).\n" +
			 "*Isolate the current archive more regularly.\n" +
			 "*Keep the To Do list minimised or closed when you're not using " +
			 "it." +
			 "*Don't apply sorts that you don't need (i.e. 3rd of 4th level " +
			 "sorts probably won't make any difference anyway).";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[5][0] = helpEntry;
		 
		 //subtopic of 'Troubleshooting':
		 subTopic = "Application/Buttons Freeze Up";
		 subTopicText = 
			 "If the application stops responding (after waiting 30 seconds or " +
			 "more), restarting it should make it work again. Avoid exiting the " +
			 "application while the application is changing a To Do (e.g. if is " +
			 "is slow writing to the log of a To Do) as exiting while the application " +
			 "is modifying the underlying data files could cause you to lose all your " +
			 "data. Again - take backups regularly to avoid this risk!";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[5][1] = helpEntry;
		 
		 //subtopic of 'Troubleshooting':
		 subTopic = "Application Minimises and Restores Itself";
		 subTopicText = 
			 "It is supposed to do this for performance reasons." +
			 "\n\n" +
			 "There is a bug with the latest Java versions that mean that " +
			 "closed JFrames (windows) don't get Garbage Collected properly. " +
			 "This Garbage Collection only happens when the application is " +
			 "minimised and restored. Since it is generally the archive " +
			 "that takes up memory and aggravates this bug, you will notice " +
			 "sometimes that when you open the archive the application quickly " +
			 "minimises and restores - this means memory was running low " +
			 "and the operation occured to Garbage Collect any memory being " +
			 "wasted.";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[5][2] = helpEntry;
		 
		 //Main topic 7 is 'Alerts'
		 arrMainTopics[6] = "Alerts";
		 
		 //need to declare size (ie number of subtopics) of helpentry array at index:
		 arrSubTopics[6] = new HelpEntry[3];
		 
		 //subtopic of 'Alerts':
		 subTopic = "Alerts";
		 subTopicText = "Alerts allow you to set reminders about the status of " +
		 		"your To Dos. They can modify the status of a To Do that you " +
		 		"know will change in the future but currently needs to stay " +
		 		"the way it is, or just give you a reminder to do a certain task." +
		 		"\n\n" +
		 		"To create an alert, select the 'Options' -> " +
		 		"'Create Alerts...' menu item from your main To Do list. This " +
		 		"will give you a list of Alert types to choose from." +
		 		"\n\n" +
		 		"To view your existing alerts, select the 'Options' -> " +
		 		"'View Alerts...' menu item from your main To Do List. Here " +
		 		"you can see the times you have set alerts for, the type of " +
		 		"alerts you have set, whether you will be notified when the " +
		 		"alert fires and which To Dos your alerts are related to. " +
		 		"You also have the option to delete existing alerts so that " +
		 		"they no longer occur or view more specific details about the " +
		 		"alerts.";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[6][0] = helpEntry;
		 
		 //subtopic of 'Alerts':
		 subTopic = "Priority Alerts";
		 subTopicText = "Priority alerts are used to change the priority of " +
		 		"a To Do at some point in the future. This can be useful " +
		 		"for when you have no actions on a To Do at present but " +
		 		"you know that at some specific future time you will need " +
		 		"to action the To Do." +
		 		"\n\n" +
		 		"To create a priority alert, you need to select a To Do " +
		 		"to associate the alert with, a priority to change that " +
		 		"To Do to and a time for this action to occur. You can " +
		 		"also choose whether or not to be notified when the change " +
		 		"is made.";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[6][1] = helpEntry;
		 
		 //subtopic of 'Alerts':
		 subTopic = "Message Alerts";
		 subTopicText = "Message alerts are simply reminders that give you " +
		 		"any message you like. They are useful for reminding you to " +
		 		"do a specific action on a To Do that doesn't change the status " +
		 		"of the To Do immeadiately." +
		 		"\n\n" +
		 		"To create a message alert, you need to select a To Do to " +
		 		"associate it with, type a message to be notified with and " +
		 		"select a time for the alert to notify you."; 
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[6][2] = helpEntry;
	 
		 //Main topic 8 is 'Version Updates'
		 arrMainTopics[7] = "Version Updates";
		 
		 //need to declare size (ie number of subtopics) of helpentry array at index:
		 arrSubTopics[7] = new HelpEntry[4];
		 
		 //subtopic of 'Version Updates':
		 subTopic = "Version 1.0";
		 subTopicText = "This was the original version, it had the following " +
		 		"features:" +
		 		"\n\n" +
		 		"*To Do List & Archive(s)\n" +
		 		"*Backup Facility\n" +
		 		"*Ability to update descriptions and delete To Dos\n" +
		 		"*Filters (date, priority)\n" +
		 		"*Sorts (date, priority, description)\n" +
		 		"*Help menu";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[7][0] = helpEntry;
		 
		 //subtopic of 'Version Updates':
		 subTopic = "Version 1.1";
		 subTopicText = "This version added the following features:" +
		 		"\n\n" +
		 		"*Filter by description (i.e. a search mechanism)\n" +
		 		"*Help menu updated to reflect this";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[7][1] = helpEntry;
		 
		 //subtopic of 'Version Updates':
		 subTopic = "Version 1.2";
		 subTopicText = "This version added the following features:" +
		 		"\n\n" +
		 		"*Alerts (Priority, Message)\n" +
		 		"*Help menu updated to reflect this";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[7][2] = helpEntry;
		 
//		subtopic of 'Version Updates':
		 subTopic = "Version 1.3";
		 subTopicText = "This version added the following features:" +
		 		"\n\n" +
		 		"*Minor bug fix (scrollbar resetting on updates)\n" +
		 		"*File filters to load/save files" +
		 		"\n\n" +
		 		"Version 1.3.1 added:" +
		 		"\n\n" +
		 		"*Prompt to lose unsaved changes on log form";
		 helpEntry = new HelpEntry(subTopic, subTopicText);
		 arrSubTopics[7][3] = helpEntry;
		  
	}
	
	/**
	 * fires when a selection is made on the main topic list, this method
	 * creates a list of sub topics and implements them to the JList
	 * @param mainSelection
	 */
	private static void setSubMenu(int mainSelection)
	{
		if (mainSelection != -1)
		{
			DefaultListModel newDLM = new DefaultListModel();
			
			HelpEntry[] helpEntries = arrSubTopics[mainSelection];
			
			for (int x=0; x<helpEntries.length; x++)
			{
				newDLM.addElement(helpEntries[x].getTopic());
			}
			
			listSub.setModel(newDLM);
		}
	}
	
	private static void setHelpText(int mainSelection, int subSelection)
	{
		if (mainSelection != -1 && subSelection != -1)
		{
			textArea.setText(arrSubTopics[mainSelection][subSelection].getText());
		}
	}

	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == bClose)
		{
			dispose();
		}
	}
	
	/**
	 * private class that reports the index of the value chosen from listMain
	 * @author Jonathan
	 */
	private class MainValueReporter implements ListSelectionListener //list listener	
	{
    	public void valueChanged(ListSelectionEvent event) //list selection made
	    {
    		setSubMenu(listMain.getSelectedIndex());
	    }
  	}
	
	/**
	 * private class that reports the index of the value chosen from listSub
	 * @author Jonathan
	 */
	private class SubValueReporter implements ListSelectionListener //list listener	
	{
    	public void valueChanged(ListSelectionEvent event) //list selection made
	    {
    		setHelpText(listMain.getSelectedIndex(), listSub.getSelectedIndex());
    		textArea.setCaretPosition(0);
	    }
  	}
}
