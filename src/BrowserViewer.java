import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.event.*;
import javax.swing.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;

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
public class BrowserViewer extends JPanel {
	// constants
	public static final Dimension SIZE = new Dimension(800, 600);
	public static final String BLANK = " ";
	public static HashMap<String,String> BUTTONLABEL=new HashMap<String,String>();
	static{
		BUTTONLABEL.put(TivooViewer.SUMMARY_PATH, "Summary and Details");
		BUTTONLABEL.put(TivooViewer.LIST_PATH, "List");
		BUTTONLABEL.put(TivooViewer.CONFILICT_PATH, "Conflict Events");
		BUTTONLABEL.put(TivooViewer.CALENDAR_PATH, "Calendar");
		
	}

	// web page
	private JEditorPane myPage;
	// information area
	private JLabel myStatus;
	// navigation
	private JButton myBackButton;
	private JButton myNextButton;
	// favorites
	// the real worker
	protected BrowserModel myModel;
	private ArrayList<String> myshowpage;

	/**
	 * Create a view of the given model of a web browser.
	 */
	public BrowserViewer(ArrayList<String> showpagepath,BrowserModel model) {
		myshowpage=showpagepath;
		myModel = model;
		// add components to frame
		setLayout(new BorderLayout());
		// must be first since other panels may refer to page
		add(makePageDisplay(), BorderLayout.CENTER);
		add(makeInputPanel(), BorderLayout.NORTH);
		add(makeInformationPanel(), BorderLayout.SOUTH);
		// control the navigation
		enableButtons();
	}

	/**
	 * Display given URL.
	 */
	public void showPage(String url) {
		try {
			// let user leave off initial protocol
			if (!url.startsWith("file:")) {
				url = "file:" + url;
			}
			// check for a valid URL before updating model, view
			new URL(url);
			// must be a valid URL, now update model and display results
			myModel.go(url);
			update(url);
		} catch (MalformedURLException e) {
			showError("Could not load " + url);
		}
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
		myStatus.setText(message);
	}

	// move to the next URL in the history
	private void next() {
		String url = myModel.next();
		if (url != null) {
			update(url);
		}
	}

	// move to the previous URL in the history
	private void back() {
		String url = myModel.back();
		if (url != null) {
			update(url);
		}
	}

	// change current URL to the home page, if set
	private void home() {
		String url = myModel.getHome();
		if (url != null) {
			showPage(url);
		}
	}

	// update just the view to display given URL
	private void update(String url) {
		try {
			myPage.setPage(url);
			enableButtons();
		} catch (IOException e) {
			// should never happen since only checked URLs make it this far ...
			showError("Could not load " + url);
		}
	}

	// prompt user for name of favorite to add to collection
	private void addFavorite() {
		String name = JOptionPane.showInputDialog(this, "Enter name",
				"Add Favorite", JOptionPane.QUESTION_MESSAGE);
		if (name != null) {
			// update model and display results
			myModel.addFavorite(name);
		}
	}

	// only enable buttons when useful to user
	private void enableButtons() {
		myBackButton.setEnabled(myModel.hasPrevious());
		myNextButton.setEnabled(myModel.hasNext());
	}

	// convenience method to create HTML page display
	private JComponent makePageDisplay() {
		// displays the web page
		myPage = new JEditorPane();
		myPage.setPreferredSize(SIZE);
		// allow editor to respond to link-clicks/mouse-overs
		myPage.setEditable(false);
		myPage.addHyperlinkListener(new LinkFollower());
		return new JScrollPane(myPage);
	}

	// organize user's options for controlling/giving input to model
	private JComponent makeInputPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		JPanel showpagepanel=new JPanel();
		
		for(final String s:myshowpage){
			JButton button=new JButton(BUTTONLABEL.get(s));
			button.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					showPage(s);
				}});
			showpagepanel.add(button);
		}
			
		panel.add(makeNavigationPanel(), BorderLayout.NORTH);
		panel.add(showpagepanel,BorderLayout.SOUTH);
		return panel;
	}

	// make the panel where "would-be" clicked URL is displayed
	private JComponent makeInformationPanel() {
		// BLANK must be non-empty or status label will not be displayed in GUI
		myStatus = new JLabel(BLANK);
		return myStatus;
	}

	// make user-entered URL/text field and back/next buttons
	private JComponent makeNavigationPanel() {
		JPanel panel = new JPanel();
		myBackButton = new JButton("Back");
		myBackButton.addActionListener(new BackPageAction());
		panel.add(myBackButton);

		myNextButton = new JButton("Next");
		myNextButton.addActionListener(new NextPageAction());
		panel.add(myNextButton);


		// if user presses return, load/show the URL

		return panel;
	}

	// make buttons for setting favorites/home URLs


	/**
	 * Inner class to factor out showing page associated with the entered URL
	 */
	private class ShowPageAction implements ActionListener {
		public void actionPerformed(ActionEvent e) {
		}
	}

	private class BackPageAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			back();
		}

	}

	private class NextPageAction implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			next();
		}

	}

	/**
	 * Inner class to deal with link-clicks and mouse-overs
	 */
	private class LinkFollower implements HyperlinkListener {
		public void hyperlinkUpdate(HyperlinkEvent evt) {
			// user clicked a link, load it and show it
			if (evt.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				showPage(evt.getURL().toString());
			}
			// user moused-into a link, show what would load
			else if (evt.getEventType() == HyperlinkEvent.EventType.ENTERED) {
				showStatus(evt.getURL().toString());
			}
			// user moused-out of a link, erase what was shown
			else if (evt.getEventType() == HyperlinkEvent.EventType.EXITED) {
				showStatus(BLANK);
			}
		}
	}
}