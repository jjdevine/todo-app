package toDo.gui.customComponents;

import java.awt.Dimension;
import java.util.Calendar;
import toDo.utilities.*;
import javax.swing.*;

import toDo.exceptions.DateParseException;

public class DateInput 
{
	private JPanel panelMain, panelLabels, panelInputs;
	private JTextField tDD, tMM, tYY, thh, tmm;
	private JLabel lDD, lMM, lYY, lhh, lmm;
	private JLabel lSlash1, lSlash2, lColon;
	
	public DateInput()
	{
		panelMain = new JPanel();
		panelLabels = new JPanel();
		panelInputs = new JPanel();
		
		panelMain.setPreferredSize(new Dimension(285,65));
		//panelMain.setBorder(new LineBorder(Color.BLACK));
		
		panelLabels.setPreferredSize(new Dimension(280,20));
		panelInputs.setPreferredSize(new Dimension(280,30));
		
		/*
		 * panelLabels
		 */
		
		lDD = new JLabel("DD");
		lMM = new JLabel("MM");
		lYY = new JLabel("YY");
		lhh = new JLabel("hh");
		lmm = new JLabel("mm");
		
		SpringLayout labelLayout = new SpringLayout();
		
		labelLayout.putConstraint(SpringLayout.WEST, lDD, 15, SpringLayout.WEST, panelLabels);
		labelLayout.putConstraint(SpringLayout.WEST, lMM, 65, SpringLayout.WEST, panelLabels);
		labelLayout.putConstraint(SpringLayout.WEST, lYY, 115, SpringLayout.WEST, panelLabels);
		labelLayout.putConstraint(SpringLayout.WEST, lhh, 195, SpringLayout.WEST, panelLabels);
		labelLayout.putConstraint(SpringLayout.WEST, lmm, 245, SpringLayout.WEST, panelLabels);
		
		panelLabels.setLayout(labelLayout);
		
		panelLabels.add(lDD);
		panelLabels.add(lMM);
		panelLabels.add(lYY);
		panelLabels.add(lhh);
		panelLabels.add(lmm);
		
		/*
		 * panelInputs
		 */
		
		lSlash1 = new JLabel("/");
		lSlash2 = new JLabel("/");
		lColon = new JLabel(":");
		
		tDD = new JTextField();
		tMM = new JTextField();
		tYY = new JTextField();
		thh = new JTextField();
		tmm = new JTextField();
		
		Dimension dInputSize = new Dimension(30,25);
		
		tDD.setPreferredSize(dInputSize);
		tMM.setPreferredSize(dInputSize);
		tYY.setPreferredSize(dInputSize);
		thh.setPreferredSize(dInputSize);
		tmm.setPreferredSize(dInputSize);
		
		SpringLayout inputLayout = new SpringLayout();
		
		inputLayout.putConstraint(SpringLayout.WEST, tDD, 10, SpringLayout.WEST, panelInputs);
		inputLayout.putConstraint(SpringLayout.WEST, lSlash1, 50, SpringLayout.WEST, panelInputs);
		inputLayout.putConstraint(SpringLayout.WEST, tMM, 60, SpringLayout.WEST, panelInputs);
		inputLayout.putConstraint(SpringLayout.WEST, lSlash2, 100, SpringLayout.WEST, panelInputs);
		inputLayout.putConstraint(SpringLayout.WEST, tYY, 110, SpringLayout.WEST, panelInputs);
		inputLayout.putConstraint(SpringLayout.WEST, thh, 190, SpringLayout.WEST, panelInputs);
		inputLayout.putConstraint(SpringLayout.WEST, lColon, 230, SpringLayout.WEST, panelInputs);
		inputLayout.putConstraint(SpringLayout.WEST, tmm, 240, SpringLayout.WEST, panelInputs);
		
		panelInputs.setLayout(inputLayout);
		
		panelInputs.add(tDD);
		panelInputs.add(lSlash1);
		panelInputs.add(tMM);
		panelInputs.add(lSlash2);
		panelInputs.add(tYY);
		panelInputs.add(thh);
		panelInputs.add(lColon);
		panelInputs.add(tmm);
		
		panelMain.add(panelLabels);
		panelMain.add(panelInputs);
	}
	
	public JPanel getComponent()
	{
		return panelMain;
	}
	
	/*
	 * returns null if nothing is input, a Calendar object if a correct
	 * date is input or throws an exception if an invalid or incomplete
	 * date is input
	 */
	public Calendar getDate() throws DateParseException
	{
		Calendar date = Calendar.getInstance();
		
		String DD = tDD.getText().trim();
		String MM = tMM.getText().trim();
		String YY = tYY.getText().trim();
		String hh = thh.getText().trim();
		String mm = tmm.getText().trim();
		
		int numDD = 0;
		int numMM = 0;
		int numYY = 0;
		int numhh = 0;
		int nummm = 0;
		
		if(DD.length() == 0 && MM.length() == 0 && YY.length() == 0 && hh.length() == 0 && mm.length() == 0)
		{
			return null;
		}
		
		try
		{
			numDD = Integer.parseInt(DD);
			numMM = Integer.parseInt(MM);
			numYY = Integer.parseInt(YY);
			numhh = Integer.parseInt(hh);
			nummm = Integer.parseInt(mm);
			
			numYY+= 2000; //convert to YYYY format
			
		}
		catch(NumberFormatException ex)
		{
			throw new DateParseException(ex.getMessage());
		}
		
		if(!ToDoUtilities.isValidDateTime(numDD,numMM, numYY, numhh, nummm))
		{
			throw new DateParseException("Not a valid date/time");
		}
		
		date.set(Calendar.YEAR, numYY);
		date.set(Calendar.MONTH, numMM-1); //calendar indexes months from 0;
		date.set(Calendar.DAY_OF_MONTH, numDD);
		date.set(Calendar.HOUR_OF_DAY, numhh);
		date.set(Calendar.MINUTE, nummm);
		
		return date;
	}
	
	public void insertTodaysDate()
	{
		Calendar c = Calendar.getInstance();
		
		int day = c.get(Calendar.DAY_OF_MONTH);
		int month = c.get(Calendar.MONTH)+1;
		int year = c.get(Calendar.YEAR);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		
		//pad out dates to two characters if required
		String strDay = (day+"").length() == 1? "0" + day: day+"";
		String strMonth = (month+"").length() == 1? "0" + month: month+"";
		String strHour = (hour+"").length() == 1? "0" + hour: hour+"";
		String strMinute = (minute+"").length() == 1? "0" + minute: minute+"";
		
		tDD.setText(strDay);
		tMM.setText(strMonth);
		tYY.setText((year+"").substring(2));//because by default year is YYYY
		thh.setText(strHour);
		tmm.setText(strMinute);
	}
	
	
}
