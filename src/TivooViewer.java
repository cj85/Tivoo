import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import javax.swing.*;

import exception.TivooIllegalDateFormat;
import exception.TivooInvalidFeed;
import exception.TivooUnrecognizedFeed;

/**
 * A class used to display the viewer for a simple HTML browser.
 * 
 * See this tutorial for help on how to use the variety of components:
 * http://docs.oracle.com/javase/tutorial/uiswing/components/index.html
 * 
 * 
 * 
 * @author Owen Astrachan
 * @author Marcin Dobosz
 * @author Robert C. Duvall
 */
@SuppressWarnings("serial")
public class TivooViewer extends JPanel {
	// constants

	public static final String BLANK = " ";// /home/chenji/workspace/Tivoo
	public static final String PATH = "./html/";
	public static final String XMLFOLDER_PATH = "./xml";
	public static final String SUMMARY_PATH = PATH + "summary.html";
	public static final String CONFILICT_PATH = PATH + "conflicts.html";
	public static final String CALENDAR_PATH = PATH + "calendar.html";
	public static final String LIST_PATH = PATH + "list.html";

	private ArrayList<String> myPathToOutPut = new ArrayList<String>();

	// information area
	private ArrayList<JLabel> myLabels = new ArrayList<JLabel>();
	private String[] infoLabel = { "Loaded File", "Added Filter",
			"Added Writer" };
	// navigation
	private JButton myGoButton;
	private JButton myLoadButton;

	private JButton mySummaryAndDetailsButton;
	private JButton myConflictButton;
	private JButton myCalendarButton;
	private JButton myListButton;
	// favorites
	private JButton myKeyWordFilterButton;
	private JButton myTimeFrameFilterButton;
	private JButton myKeyWordSortingButton;
	private JButton myKeyWordListButton;
	// the real worker
	protected TivooSystem myModel;

	/**
	 * Create a view of the given model of a web browser.
	 */
	public TivooViewer(TivooSystem model) {
		myModel = model;
		for (int i = 0; i < infoLabel.length; ++i)
			myLabels.add(new JLabel());
		setLayout(new BorderLayout());
		add(makeOperatePanel(), BorderLayout.NORTH);
		add(makeInformationPanel());
		enableButtons();
	}

	/**
	 * Display given message as an error in the GUI.
	 */
	public void showError(String message) {
		JOptionPane.showMessageDialog(this, message, "Browser Error",
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Display given message as information in the GUI.
	 */
	public void showStatus(String message) {

	}

	private void loadfile() {
		try {
			JFileChooser fc = new JFileChooser(XMLFOLDER_PATH);
			int returnVal = fc.showOpenDialog(null);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				myModel.loadFile(file);

			}
		} catch (TivooInvalidFeed e) {
			JOptionPane.showMessageDialog(myLoadButton,
					"File Type Err, Please Load a XML File");
		} catch (TivooUnrecognizedFeed e) {
			JOptionPane.showMessageDialog(myLoadButton,
					"Can't Deal With This XML File, No Correct Parse Exist");
		}
	}

	/**
	 * only enable buttons when useful to user
	 * */
	private void enableButtons() {

		myGoButton.setEnabled(myModel.readyToGo());
		mySummaryAndDetailsButton
				.setEnabled(myModel.summaryAndDetailPagesWriterFlag);
		myConflictButton.setEnabled(myModel.conflictWriterFlag);
		myCalendarButton.setEnabled(myModel.calendarWriterFlag);
		myListButton.setEnabled(myModel.listWriterNumber);
	}

	private JComponent makeOperatePanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 3));
		panel.add(makeLoadAndOutputPanel());
		panel.add(makeFilterPanel());
		panel.add(makeWriterPanel());
		return panel;
	}

	private JComponent makeInformationPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel infoPanel = new JPanel();
		infoPanel.setLayout(new GridLayout(1, 3));
		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new GridLayout(1, 3));
		Dimension infoLabelSize = new Dimension(200, 200);
		Dimension labelSize = new Dimension(200, 30);
		for (int i = 0; i < myLabels.size(); i++) {
			myLabels.get(i).setPreferredSize(infoLabelSize);
			myLabels.get(i).setVerticalAlignment(SwingConstants.TOP);
			infoPanel.add(myLabels.get(i));
			JLabel font = new JLabel();
			font.setText("<html><body><font  size='5'>" + infoLabel[i]
					+ "</font></body></html>");
			font.setPreferredSize(labelSize);
			font.setHorizontalAlignment(SwingConstants.CENTER);
			labelPanel.add(font);
		}
		mainPanel.add(labelPanel, BorderLayout.NORTH);
		mainPanel.add(infoPanel, BorderLayout.SOUTH);
		return mainPanel;
	}

	private JComponent makeLoadAndOutputPanel() {
		JPanel panel = new JPanel();
		myLoadButton = new JButton("Load");
		myLoadButton.addActionListener(new LoadFileAction());

		panel.add(myLoadButton);

		myGoButton = new JButton("Go");
		myGoButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				myModel.perform();
				displayinBrowser(myPathToOutPut);
			}
		});
		panel.add(myGoButton);

		return panel;
	}

	private JComponent makeFilterPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 1));
		myKeyWordFilterButton = new JButton("Add KeywordFilter");
		myKeyWordFilterButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				myModel.addFilterByKeyword(JOptionPane
						.showInputDialog("please input keyword"));
				update();
			}
		});
		panel.add(myKeyWordFilterButton, BorderLayout.NORTH);

		myTimeFrameFilterButton = new JButton("Add TimeFrameFilter");
		myTimeFrameFilterButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					String startTime = JOptionPane
							.showInputDialog("please input startTime\n yyyy-MM-dd HH:mm:ss");
					if (startTime == null || startTime.equalsIgnoreCase(""))
						return;
					myModel.checkDateFormat(startTime);
					String endTime = JOptionPane
							.showInputDialog("please input end\n yyyy-MM-dd HH:mm:ss");
					myModel.checkDateFormat(endTime);
					myModel.addFilterByTimeFrame(startTime, endTime);
				} catch (ParseException excp) {
					JOptionPane.showMessageDialog(myTimeFrameFilterButton,
							"Please In Put Correct Time Format");
				}
				update();
			}
		});
		panel.add(myTimeFrameFilterButton);

		myKeyWordSortingButton = new JButton("Add Field Sorter");
		myKeyWordSortingButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String keywordtosort = JOptionPane
						.showInputDialog("please input the Field to sort\n ex. title, startTime, endTime, summary etc");
				myModel.addFilterByKeywordSorting(keywordtosort);
				update();
			}
		});
		panel.add(myKeyWordSortingButton);

		myKeyWordListButton = new JButton("Add WordList Filter");
		myKeyWordListButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ArrayList<String> keywordlist = new ArrayList<String>();
				int i = 1;
				String keyword;
				while ((keyword = JOptionPane.showInputDialog("please input "
						+ i + "th keyword")) != null
						&& !keyword.equalsIgnoreCase("")) {
					i++;
					keywordlist.add(keyword);
				}

				myModel.addFilterByKeywordList(keywordlist);
				update();
			}
		});
		panel.add(myKeyWordListButton);
		return panel;
	}

	private JComponent makeWriterPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 1));
		mySummaryAndDetailsButton = new JButton("Add SummaryandDetails Writer");
		mySummaryAndDetailsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				myModel.addSummaryAndDetailPagesWriter(SUMMARY_PATH);
				myPathToOutPut.add(SUMMARY_PATH);
				update();
			}
		});
		panel.add(mySummaryAndDetailsButton);

		myConflictButton = new JButton("Add Conflicts Writer");
		myConflictButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				myModel.addConflictWriter(CONFILICT_PATH);
				myPathToOutPut.add(CONFILICT_PATH);
				update();
			}
		});
		panel.add(myConflictButton);

		myCalendarButton = new JButton("Add Calendar Writer");
		myCalendarButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String startTime = JOptionPane
						.showInputDialog("please input startTime yyyy-MM-dd HH:mm:ss");
				if (startTime == null || startTime.equalsIgnoreCase(""))
					return;
				try {
					myModel.checkDateFormat(startTime);
					String period = JOptionPane
							.showInputDialog("please input Time Frame\nMONTH or WEEK or DAY");
					myModel.checkDateFrame(period);
					myModel.addCalendarWriter(CALENDAR_PATH, startTime, period);
					myPathToOutPut.add(CALENDAR_PATH);
					update();
				} catch (ParseException e1) {
					JOptionPane.showMessageDialog(myCalendarButton,
							"Please In Put Correct Time Format");
					
				}
				catch(TivooIllegalDateFormat e2){
					JOptionPane.showMessageDialog(null,
							"Please In Put Correct TimeFrame: month, week or day");
				}
			}
		});
		panel.add(myCalendarButton);

		myListButton = new JButton("Add List Writer");
		myListButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				myModel.addListWriter(LIST_PATH);
				myPathToOutPut.add(LIST_PATH);
				update();
			}
		});
		panel.add(myListButton);

		return panel;

	}

	/**
	 * Inner class to factor out showing page associated with the entered URL
	 */

	private class LoadFileAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			loadfile();
			update();
		}

	}

	// twoDimentionalList must obey the infoLabel
	private void update() {
		ArrayList<ArrayList<String>> twoDimentionalList = new ArrayList<ArrayList<String>>();
		twoDimentionalList.add(myModel.getLoadedFile());
		twoDimentionalList.add(myModel.getAddFilter());
		twoDimentionalList.add(myModel.getAddedWriter());
		for (ArrayList<String> list : twoDimentionalList) {
			String toSet = "<html><body>";
			for (String s : list)
				toSet += "-------------<br>" + s + "<br>";
			toSet += "</body> </html>";
			myLabels.get(twoDimentionalList.indexOf(list)).setText(toSet);
		}
		enableButtons();
	}

	private void displayinBrowser(ArrayList<String> showpagepath) {
		BrowserModel model = new BrowserModel();
		BrowserViewer display = new BrowserViewer(showpagepath, model);
		// create container that will work with Window manager
		JFrame frame = new JFrame("NanoBrowser");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// add our user interface components to Frame and show it
		frame.getContentPane().add(display);
		frame.pack();
		frame.setVisible(true);
		// start somewhere, less typing for debugging
		display.showPage(showpagepath.get(0));
	}
}