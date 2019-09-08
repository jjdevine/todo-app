package toDo.gui.filterSort;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import toDo.filters.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

public class DescriptionFilterConfigure extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private FilterSortMenu sourceWindow;
	private int sWidth = 350, sHeight = 255;
	private JPanel panelText, panelOptions, panelButtons;
	private JLabel lSearch;
	private JTextArea tSearch;
	private JCheckBox cbCase, cbAllTerms;
	private JButton bConfirm, bCancel;
	
	public DescriptionFilterConfigure(FilterSortMenu sourceWindow)
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
		
		panelText = new JPanel();
		panelOptions = new JPanel();
		panelButtons = new JPanel();
		
		panelText.setPreferredSize(new Dimension(sWidth-20, 90));
		panelOptions.setPreferredSize(new Dimension(sWidth-20, 70));
		panelButtons.setPreferredSize(new Dimension(sWidth-20, 37));
		
		panelText.setBorder(new LineBorder(Color.BLACK));
		panelOptions.setBorder(new LineBorder(Color.BLACK));
		panelButtons.setBorder(new LineBorder(Color.BLACK));
		
		/*
		 * panel text
		 */
		
		lSearch = new JLabel("Enter keywords : ");
		lSearch.setFont(new Font("Arial" , Font.PLAIN, 16));
		lSearch.setForeground(Color.RED);
		
		tSearch = new JTextArea();
		tSearch.setLineWrap(true);
		tSearch.setWrapStyleWord(true);
		
		JScrollPane jsp = new JScrollPane(tSearch);
		jsp.setPreferredSize(new Dimension(sWidth-40, 40));
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		panelText.add(lSearch);
		panelText.add(jsp);
		
		/*
		 * panel options
		 */
		
		cbCase = new JCheckBox("Make Search Case Sensitive");
		cbAllTerms = new JCheckBox("All Keywords Required for Match");
		
		cbCase.setSelected(false);
		cbAllTerms.setSelected(false);
		
		cbCase.setPreferredSize(new Dimension(sWidth-40,25));
		cbAllTerms.setPreferredSize(new Dimension(sWidth-40,25));
		
		panelOptions.add(cbCase);
		panelOptions.add(cbAllTerms);

		/*
		 * panel buttons
		 */
		
		bConfirm = new JButton("Confirm");
		bCancel = new JButton("Cancel");
		
		Dimension dButton = new Dimension((sWidth-50)/2, 25);
		
		bConfirm.setPreferredSize(dButton);
		bCancel.setPreferredSize(dButton);
		
		bConfirm.addActionListener(this);
		bCancel.addActionListener(this);
		
		panelButtons.add(bConfirm);
		panelButtons.add(bCancel);
		
		/*
		 * add to container
		 */
		
		container.add(panelText);
		container.add(panelOptions);
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
			if(tSearch.getText().trim() != "")
			{
				sendFilterToSource();
				dispose();
			}
			else
			{
				JOptionPane.showMessageDialog(null, "You must enter some keywords to search for!", "error", JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(e.getSource() == bCancel)
		{
			dispose();
		}	
	}
	
	public void sendFilterToSource()
	{
		DescriptionFilter descFilter = new DescriptionFilter();
		
		descFilter.setCaseSensitive(cbCase.isSelected());
		descFilter.setNeedAllSearchTerms(cbAllTerms.isSelected());
		
		//get text and replace newlines with spaces
		String keywords = tSearch.getText().replaceAll("\n", " ");
		
		descFilter.setSearchString(keywords, " ");
		
		sourceWindow.addFilter(descFilter);
	}

}
