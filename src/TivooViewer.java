import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.PopupMenu;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

	public static final String BLANK = " ";
	public static final String SUMMARY_PATH = "/home/chenji/workspace/Tivoo/html/summary.html";

	// information area
	private ArrayList<JLabel> myLabels = new ArrayList<JLabel>();
	private String[] infoLabel = { "LoadedFile", "AddedFilter", "AddedWriter" };
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

	// organize user's options for controlling/giving input to model
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
		Dimension infoLabelSize = new Dimension(200, 300);
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
//twoDimentionalList must obey the infoLabel
	private void update() {
		ArrayList<ArrayList<String>> twoDimentionalList = new ArrayList<ArrayList<String>>();
		twoDimentionalList.add(myModel.getLoadedFile());
		twoDimentionalList.add(myModel.getAddFilter());
		twoDimentionalList.add(myModel.getAddedWriter());
		for (ArrayList<String> list : twoDimentionalList) {
			String toSet = "<html><body>";
			for (String s : list)
				toSet += "" + s + "<br>";
			toSet += "</body> </html>";
			myLabels.get(twoDimentionalList.indexOf(list)).setText(toSet);
		}
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