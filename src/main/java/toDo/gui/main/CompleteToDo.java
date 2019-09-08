package toDo.gui.main;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;

import toDo.gui.customComponents.ToDoHolder;
import toDo.interfaces.Alertable;

public class CompleteToDo extends JFrame implements ActionListener
{

	private static final long serialVersionUID = -9189568845112343136L;
	private int sWidth = 500, sHeight = 375;
	private JLabel lHeader, lComments;
	private JTextArea taComments;
	private JButton bComplete, bCancel;
	private JPanel panelHeader, panelMain;
	private ToDoHolder holder;
	private MainGui main;
	private Alertable alertable;

	public CompleteToDo(ToDoHolder holder, MainGui m, Alertable a)
	{
		super("Completed To Do Item");	//form heading
		//create container to place components in:
		Container container = getContentPane();
		container.setLayout(new FlowLayout());	//set flow layout
		
		//store references
		this.holder = holder;
		main = m;
		alertable = a;
		
		panelHeader = new JPanel();
		panelMain = new JPanel();
		
		panelHeader.setPreferredSize(new Dimension(470,50));
		panelMain.setPreferredSize(new Dimension(470,265));
		
		panelHeader.setBorder(new LineBorder(Color.BLACK));
		panelMain.setBorder(new LineBorder(Color.BLACK));
		
		/*
		 * header panel
		 */
		
		String desc = holder.getDescription();
		
		if (desc.length() > 20)
		{
			desc = desc.substring(0,18) + "...";
		}
		
		lHeader = new JLabel("Completed : " + desc);
		
		lHeader.setFont(new Font("Comic Sans MS" , Font.BOLD, 24));
		
		panelHeader.add(lHeader);
		
		/*
		 * main panel
		 */
		
		lComments = new JLabel("Closing comments : ");
		
		taComments = new JTextArea();
		//taComments.setPreferredSize(new Dimension(450,200));
		taComments.setBorder(new LineBorder(Color.BLACK));
		taComments.setLineWrap(true);
		taComments.setWrapStyleWord(true);
		
		JScrollPane jsp = new JScrollPane(taComments);
		jsp.setPreferredSize(new Dimension(450,200));
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		bComplete = new JButton("Mark Complete");
		bCancel = new JButton("Cancel");
		
		bComplete.setPreferredSize(new Dimension(220,25));
		bCancel.setPreferredSize(new Dimension(220,25));
		
		bComplete.addActionListener(this);
		bCancel.addActionListener(this);
		
		panelMain.add(lComments);
		panelMain.add(jsp);
		panelMain.add(bComplete);
		panelMain.add(bCancel);
		
		/*
		 * add panels to container
		 */
		
		container.add(panelHeader);
		container.add(panelMain);

		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();	//get screen resoloution
		setLocation((d.width-sWidth)/2, (d.height-sHeight)/2);	//centre form
		
		setSize(sWidth,sHeight);	//set form size
		setVisible(true);//display screen
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == bComplete)
		{
			alertable.removeAllAlertsForID(holder.getToDoItem().getId());
			holder.markComplete(taComments.getText().trim());
			main.archiveCompletedToDos(main.getToDoList(), false);
			main.refreshToDoDisplay();			
			dispose();
		}
		
		if (e.getSource() == bCancel)
		{
			dispose();
		}
	}

}
