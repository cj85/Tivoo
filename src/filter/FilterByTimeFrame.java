package filter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import event.Event;
import exception.TivooIllegalDateFormat;
import exception.TivooSystemError;


public class FilterByTimeFrame extends FilterDecorator
{

    private static DateFormat defaultDateFormat =
        new SimpleDateFormat(Event.dateFormat);

    private Date myStartTime, myEndTime;

    public final String myName="Filter by Time Frame :";
    public FilterByTimeFrame (String startTime, String endTime)
    {
        super();
        Date start, end;
        try
        {
            start = defaultDateFormat.parse(startTime);
            end = defaultDateFormat.parse(endTime);
        }
        catch (ParseException e)
        {
            throw new TivooIllegalDateFormat();
        }
        myStartTime = start;
        myEndTime = end;
    }


    @Override
    public void filter (List<Event> list)
    {
        List<Event> decoratedList = decoratedFilterWork(list);
        for (Event entry : decoratedList)
        {
            if (isWithinTimeFrame(entry))
            {
                myFilteredList.add(entry);
            }
        }
    }


    /**
     * Returns if the input event happened between the givin time frame. 
     * @param event
     * @return
     */
    public boolean isWithinTimeFrame (Event event)
    {
        DateFormat format = new SimpleDateFormat(Event.dateFormat);
        Date eventStartTime;
        Date eventEndTime;
        try
        {
            eventStartTime = format.parse(event.get("startTime"));
            eventEndTime = format.parse(event.get("endTime"));
            return (eventStartTime.after(myStartTime) && eventEndTime.before(myEndTime));
        }
        catch (ParseException e)
        {
            throw new TivooSystemError("isWithinTimeFrame failed");
        }
    }
    @Override
    public String getInformation() {
    // TODO Auto-generated method stub
    DateFormat eventFormat = new SimpleDateFormat(Event.dateFormat);
    // System.out.println(myName+"from "+eventFormat.format(myStartTime)+" to "+eventFormat.format(myEndTime)+"-----");////////////////
    return myName+"from "+eventFormat.format(myStartTime)+" to "+eventFormat.format(myEndTime);
    }
}
