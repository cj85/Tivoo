package writer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.hp.gagawa.java.elements.Body;
import com.hp.gagawa.java.elements.Html;
import com.hp.gagawa.java.elements.Table;
import com.hp.gagawa.java.elements.Td;
import com.hp.gagawa.java.elements.Text;
import com.hp.gagawa.java.elements.Tr;
import event.Event;

public class ListWriter extends Writer {
	private static int existingWriterNumber = 0;

	public static int getWriterNumber() {
		return existingWriterNumber;
	}

	public ListWriter(String filename) {
		existingWriterNumber++;
		setMyDirectory(filename);
		setMyTitle("Sorted List");
	}

	@Override
	public void outputHTML(List<Event> events) {
		Collections.sort(events, new Comparator<Event>() {
			public int compare(Event e1, Event e2) {
				return e1.get("startTime").compareTo(e2.get("startTime"));
			}
		});
		ArrayList<Object> htmlAndbody = initializeHTMLDocument();
		Html html = (Html) htmlAndbody.get(0);
		Body body = (Body) htmlAndbody.get(1);

		Table table = new Table();
		for (Event event : events) {
			Tr event_format = new Tr();

			event_format.appendChild((new Td()).appendChild(new Text(event
					.get("title"))));
			event_format.appendChild((new Td()).appendChild(new Text(event.get(
					"startTime").toString())));
			event_format.appendChild((new Td()).appendChild(new Text(event.get(
					"endTime").toString())));

			table.appendChild(event_format);
		}
		body.appendChild(table);
		write(html, getMyDirectory());
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "ListWriter";
	}

}
