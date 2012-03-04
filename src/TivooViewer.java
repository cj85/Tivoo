import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;

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

	public static final String BLANK = " ";
	public static final String PATH = "/home/chenji/workspace/Tivoo/html/";
	public static final String SUMMARY_PATH = PATH + "summary.html";
	public static final String CONFILICT_PATH = PATH + "conflicts.html";
	public static final String CALENDAR_PATH = PATH + "calendar.html";
	public static final String LIST_PATH = PATH + "list.html";

	private ArrayList<String> myPathToOutPut = new ArrayList<String>();

	// information area
	private ArrayList<JLabel> myLabels = new ArrayList<JLabel>();
	private String[] infoLabel = { "LoadedFile", "AddedFilter", "AddedWriter" };
	// navigation
	private JButton myLoadButton;
	private JButton mySummaryAndDetailsButton;
	private JButton myConflictButton;
	private JButton myCalendarButton;
	private JButton myListButton;
	// favorites
	private JButton myKeyWordFilterButton;
	private JButton myTimeFrameFilterButton;
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
		add(makeInformationPanel(), BorderLayout.SOUTH);
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
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			myModel.loadFile(file);

		}
	}

	/**
	 * only enable buttons when useful to user
	 * */
	private void enableButtons() {
		myLoadButton.setEnabled(true);
		mySummaryAndDetailsButton.setEnabled(true);
	}

	private JComponent makeOperatePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(makeLoadAndOutputPanel(), BorderLayout.WEST);
		panel.add(makeFilterPanel());
		panel.add(makeWriterPanel(), BorderLayout.EAST);
		return panel;
	}

	private JComponent makeInformationPanel() {
		JPanel mainPanel = new JPanel(new BorderLayout());
		JPanel infoPanel = new JPanel();
		JPanel labelPanel = new JPanel();
		Dimension infoLabelSize = new Dimension(200, 200);
		Dimension labelSize = new Dimension(200, 20);
		for (int i = 0; i < myLabels.size(); i++) {
			myLabels.get(i).setPreferredSize(infoLabelSize);
			myLabels.get(i).setVerticalAlignment(SwingConstants.TOP);
			infoPanel.add(myLabels.get(i));
			JLabel j = new JLabel(infoLabel[i]);
			j.setPreferredSize(labelSize);
			labelPanel.add(j);
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

		JButton goButton = new JButton("Go");
		goButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				myModel.perform();
					displayinBrowser(myPathToOutPut);
			}
		});
		panel.add(goButton);

		return panel;
	}

	private JComponent makeFilterPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4,1));
		myKeyWordFilterButton = new JButton("Add KeywordFilter");
		myKeyWordFilterButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
				String startTime = JOptionPane
						.showInputDialog("please input startTime\n yyyy-MM-dd HH:mm:ss");
				String endTime = JOptionPane
						.showInputDialog("please input end\n yyyy-MM-dd HH:mm:ss");
				myModel.addFilterByTimeFrame(startTime, endTime);

				update();
			}
		});
		panel.add(myTimeFrameFilterButton);
		return panel;
	}

	private JComponent makeWriterPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 1));
		mySummaryAndDetailsButton = new JButton("Add SummaryandDetails Writer");
		mySummaryAndDetailsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
				myModel.addCalendarWriter(
						CALENDAR_PATH,
						JOptionPane
								.showInputDialog("please input startTime yyyy-MM-dd HH:mm:ss"),
						JOptionPane
								.showInputDialog("please input Time Frame\nMONTH or WEEK or DAY"));
				myPathToOutPut.add(CALENDAR_PATH);
				update();
			}
		});
		panel.add(myCalendarButton);

		myListButton = new JButton("Add List Writer");
		myListButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub
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
	}

	private void displayinBrowser(ArrayList<String> showpagepath) {
		BrowserModel model = new BrowserModel();
		BrowserViewer display = new BrowserViewer(showpagepath,model);
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