package writer;

import java.util.List;

import com.hp.gagawa.java.elements.Body;
import com.hp.gagawa.java.elements.Html;
import com.hp.gagawa.java.elements.Table;
import com.hp.gagawa.java.elements.Td;
import com.hp.gagawa.java.elements.Text;
import com.hp.gagawa.java.elements.Tr;
import event.Event;


public class ConflictWriter extends Writer
{
    public ConflictWriter (String filename)
    {
        setMyDirectory(filename);
        setMyTitle("Conflict List");
    }


    @Override
    public void outputHTML (List<Event> events)
    {

        Html html = initializeHTMLDocument();
        Body body = new Body();
        html.appendChild(body);
        Table table = new Table();
        for (Event event : events)
        {
            if (hasConflict(event, events))
            {

                Tr event_format = new Tr();

                event_format.appendChild((new Td()).appendChild(new Text(event.get("title"))));
                event_format.appendChild((new Td()).appendChild(new Text(event.get("startTime")
                                                                              .toString())));
                event_format.appendChild((new Td()).appendChild(new Text(event.get("endTime")
                                                                              .toString())));

                table.appendChild(event_format);
            }
        }
        body.appendChild(table);
        write(html, getMyDirectory());
    }

    /**
     * Iterates through the set of events and checks if a given event overlaps with those. If it does, return false.
     * @param event
     * @param events
     * @return
     */
    private boolean hasConflict (Event event, List<Event> events)
    {

        String start = event.get("startTime");
        String end = event.get("endTime");
        for (Event other : events)
        {
            if (other != event)
            {
                String otherStart = other.get("startTime");
                String otherEnd = other.get("endTime");
                if ((otherStart.compareTo(end) <= 0 && start.compareTo(otherStart) <= 0) ||
                    (otherStart.compareTo(start) <= 0 && start.compareTo(otherEnd) <= 0)) return true;

            }
        }
        return false;
    }


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Conflicts Writer";
	}
}
