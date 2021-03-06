package writer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.hp.gagawa.java.elements.Body;

import java.util.PropertyResourceBundle;

import com.hp.gagawa.java.elements.H2;
import com.hp.gagawa.java.elements.Html;
import com.hp.gagawa.java.elements.Table;
import com.hp.gagawa.java.elements.Text;
import event.Event;
import exception.TivooEventKeywordNotFound;

public class SummaryAndDetailsPagesWriter extends Writer {
	private static int existingWriterNumber = 0;

	public static int getWriterNumber() {
		return existingWriterNumber;
	}

	private String myEventDateFormat = PropertyResourceBundle.getBundle(
			"myProperties_en").getString("dateFormat");

	public SummaryAndDetailsPagesWriter(String directory) {
		existingWriterNumber++;
		setMyTitle("Summary and Details");
		setMyDirectory(directory);
	}

	public void outputHTML(List<Event> events) {
		String directory = getMyDirectory();
		outputSummaryPage(events, directory);
		String s[] = directory.split("/");
		String ss = directory.substring(0, directory.indexOf(s[s.length - 1]));
		outputDetailsPages(events, ss);
	}

	public void outputDetailsPages(List<Event> events, String directory) {
		for (Event event : events) {
			ArrayList<Object> htmlAndbody = initializeHTMLDocument();

			Html html = (Html) htmlAndbody.get(0);
			Table details = new Table();
			Body body = (Body) htmlAndbody.get(1);
			html.appendChild(body);
			try {
				body.appendChild(new H2().appendChild(new Text(event
						.get("title"))));

				generateDetail(details, "Start Time", event.get("startTime"));
				generateDetail(details, "End Time", event.get("endTime"));
				generateDetail(details, "Summary", event.get("summary"));
			} catch (TivooEventKeywordNotFound e) {
			}

			String filename = directory + "event" + events.indexOf(event)
					+ ".html";

			body.appendChild(details);
			write(html, filename);
		}
	}

	public void outputSummaryPage(List<Event> events, String filename) {
		ArrayList<Object> htmlAndbody = initializeHTMLDocument();
		Html html = (Html) htmlAndbody.get(0);
		Body body = (Body) htmlAndbody.get(1);
		// html.appendChild(body);
		String[] days = { "Sunday", "Monday", "Tuesday", "Wednesday",
				"Thursday", "Friday", "Saturday" };

		HashMap<Integer, ArrayList<Event>> eventsByDate = groupByDate(events);
		for (int i = 0; i < 7; i++) {
			ArrayList<Event> day = eventsByDate.get(i);
			Table table = new Table();
			for (Event event : day) {
				addEventToTable(events, table, event);
			}
			Text dayOfTheWeek = new Text(days[i]);
			body.appendChild(new H2().appendChild(dayOfTheWeek));
			body.appendChild(table);

		}
		write(html, filename);
	}

	@SuppressWarnings("deprecation")
	private HashMap<Integer, ArrayList<Event>> groupByDate(List<Event> events) {
		HashMap<Integer, ArrayList<Event>> dateSet = new HashMap<Integer, ArrayList<Event>>();
		for (int i = 0; i < 7; i++)
			dateSet.put(i, new ArrayList<Event>());

		SimpleDateFormat dateCreator = new SimpleDateFormat(myEventDateFormat);
		for (Event event : events) {
			try {
				dateSet.get(dateCreator.parse(event.get("startTime")).getDay())
						.add(event);
			} catch (ParseException e) {
			}
		}
		return dateSet;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Summary And Details";
	}

}
