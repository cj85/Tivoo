package writer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import com.hp.gagawa.java.elements.Html;
import com.hp.gagawa.java.elements.Table;
import com.hp.gagawa.java.elements.Td;
import com.hp.gagawa.java.elements.Text;
import com.hp.gagawa.java.elements.Tr;
import event.Event;
import exception.TivooIllegalDateFormat;
import filter.FilterByTimeFrame;
import filter.FilterDecorator;


public class CalendarWriter extends Writer
{

    String myStartDate;
    String myTimeFrame;


    public CalendarWriter (String filename, String date, String timeFrame)
    {
        setMyDirectory(filename);
        setMyTitle("Calendar Writer");
        myStartDate = date;
        myTimeFrame = timeFrame;
    }


    @Override
    public void outputHTML (List<Event> events)
    {

        String myEndDate = processDate();
        FilterDecorator myFilter =
            new FilterByTimeFrame(myStartDate, myEndDate);
        myFilter.filter(events);
        events = myFilter.getFilteredList();

        Html html = initializeHTMLDocument();

        Table table = new Table();
        for (Event event : events)
        {
            Tr event_format = new Tr();

            event_format.appendChild((new Td()).appendChild(new Text(event.get("title"))));
            event_format.appendChild((new Td()).appendChild(new Text(event.get("startTime")
                                                                          .toString())));
            event_format.appendChild((new Td()).appendChild(new Text(event.get("endTime")
                                                                          .toString())));

            table.appendChild(event_format);
        }
        html.appendChild(table);
        write(html, getMyDirectory());
    }

    /**
     * This method processes the start date by incrementing it by the specified increment in time frame.
     * @return
     */
    private String processDate ()
    {
        DateFormat format = new SimpleDateFormat(Event.dateFormat);
        Date start = new Date();
        try
        {
            start = format.parse(myStartDate);
        }
        catch (ParseException e)
        {
            throw new TivooIllegalDateFormat();
        }

        Calendar calendar = new GregorianCalendar();
        calendar.setTime(start);

        int timeFrame = -1;
        if (myTimeFrame.equals("DAY")) timeFrame = Calendar.DAY_OF_MONTH;
        else if (myTimeFrame.equals("WEEK")) timeFrame = Calendar.WEEK_OF_YEAR;
        else if (myTimeFrame.equals("MONTH")) timeFrame = Calendar.MONTH;
        
        calendar.add(timeFrame, 1);

        return format.format(calendar.getTime());
    }


	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "CalendarWriter";
	}

}
