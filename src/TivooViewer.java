import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.PopupMenu;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.event.*;
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
	public static final Dimension SIZE = new Dimension(120, 300);
	public static final String BLANK = " ";
	public static final String SUMMARY_PATH = "/home/chenji/workspace/Tivoo/html/summary.html";

	// information area
	private JLabel myLoadedFile;
	private JLabel myAddedFilter;
	private JLabel myAddedWriter;
	// navigation
	private JButton myLoadButton;
	private JButton mySummaryAndDetailsButton;
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
		// add components to frame
		setLayout(new BorderLayout());//
		// must be first since other panels may refer to page
		add(makeOperatePanel(), BorderLayout.NORTH);
		add(makeInformationPanel(), BorderLayout.SOUTH);
		// control the navigation
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

	// organize user's options for controlling/giving input to model
	private JComponent makeOperatePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(makeLoadAndOutputPanel(), BorderLayout.NORTH);
		panel.add(makeFilterPanel());
		panel.add(makeWriterPanel(), BorderLayout.SOUTH);
		return panel;
	}

	// make the panel where "would-be" clicked URL is displayed
	private JComponent makeInformationPanel() {
		// BLANK must be non-empty or status label will not be displayed in GUI
		JPanel panel = new JPanel();
		myLoadedFile = new JLabel("LoadedFile");
		myLoadedFile.setPreferredSize(SIZE);
		myAddedFilter = new JLabel("AddedFilter");
		myAddedFilter.setPreferredSize(SIZE);
		myAddedWriter = new JLabel("AddedWriter");
		myAddedWriter.setPreferredSize(SIZE);
		panel.add(myLoadedFile);
		panel.add(myAddedFilter);
		panel.add(myAddedWriter);
		return panel;
	}

	// make user-entered URL/text field and back/next buttons
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
				displayinBrowser(SUMMARY_PATH);
			}
		});
		panel.add(goButton);
		JButton testButton = new JButton("test");
		testButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				update();
			}
		});
		panel.add(testButton);
		return panel;
	}

	// make buttons for setting favorites/home URLs
	private JComponent makeFilterPanel() {
		JPanel panel = new JPanel();

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
		panel.add(myKeyWordFilterButton);

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
		mySummaryAndDetailsButton = new JButton("Add SummaryandDetails Writer");
		mySummaryAndDetailsButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				myModel.addSummaryAndDetailPagesWriter(SUMMARY_PATH);
				update();
			}
		});
		panel.add(mySummaryAndDetailsButton);
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

	private void update() {
		String loadedfile = "<html><body>";
		String addedfilter = "<html><body>";
		String addedwriter = "<html><body>";
		for (String s : myModel.getLoadedFile())
			loadedfile += "" + s + "<br>";
		if (myModel.getAddFilter() != null)
			for (String s : myModel.getAddFilter())
				addedfilter += "" + s + "<br>";
		for (String s : myModel.getAddedWriter())
			addedwriter += "" + s + "<br>";
		loadedfile += "</body> </html>";
		addedfilter += "</body> </html>";
		addedwriter += "</body> </html>";
		myLoadedFile.setText(loadedfile);
		myAddedFilter.setText(addedfilter);
		myAddedWriter.setText(addedwriter);

	}

	private void displayinBrowser(String s) {
		BrowserModel model = new BrowserModel();
		BrowserViewer display = new BrowserViewer(model);
		// create container that will work with Window manager
		JFrame frame = new JFrame("NanoBrowser");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// add our user interface components to Frame and show it
		frame.getContentPane().add(display);
		frame.pack();
		frame.setVisible(true);
		// start somewhere, less typing for debugging
		display.showPage(s);
	}
}