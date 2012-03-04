package parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.PropertyResourceBundle;
import org.w3c.dom.Node;
import event.Event;


public class GoogleCalendarParserChen extends Parser
{
    private String myRepeatPeriod = "none";
    private static HashMap<String, String> myMonth =
        new HashMap<String, String>();
    static
    {
        myMonth.put("jan", "01");
        myMonth.put("feb", "02");
        myMonth.put("mar", "03");
        myMonth.put("apr", "04");
        myMonth.put("may", "05");
        myMonth.put("jun", "06");
        myMonth.put("jul", "07");
        myMonth.put("aug", "08");
        myMonth.put("sep", "09");
        myMonth.put("oct", "10");
        myMonth.put("nov", "11");
        myMonth.put("dec", "12");
    }


    @Override
    protected String getHead ()
    {

        return "/feed/entry";
    }


    protected String getTitle (Node currentEvent)
    {

        return getTagValue(currentEvent, "title/text()");
    }


    protected String getURL (Node currentEvent)
    {
        return getTagValue(currentEvent, "link[1]/attribute::href");
    }


    protected String getStartDate (Node currentEvent)
    {
        try
        {
            String toparse = getTagValue(currentEvent, "summary/text()");
            return parseTime(toparse, "start");

        }
        catch (java.lang.StringIndexOutOfBoundsException e)
        {

        }
        catch (ParseException e)
        {

            e.getStackTrace();
        }
        return "INVALID";
    }


    protected String getEndDate (Node currentEvent)
    {
        try
        {
            String toparse = getTagValue(currentEvent, "summary/text()");

            return parseTime(toparse, "end");
        }
        catch (java.lang.StringIndexOutOfBoundsException e)
        {

        }
        catch (ParseException e)
        {
            e.getStackTrace();
        }
        return "INVALID";
    }


    protected String getRepeatPeriod (Node currentEvent)
    {
        return myRepeatPeriod;
    }


    private String parseTime (String s, String startorend)
        throws ParseException
    {
        if (s.startsWith("Recurring"))
        {
            return recurringTime(s, startorend);
        }
        return simpleTime(s, startorend);
    }


    private String recurringTime (String s, String startorend)
        throws ParseException
    {
        myRepeatPeriod = "weekly";
        String[] timeElement = s.split("<br>");
        int start = timeElement[1].indexOf(":");

        String startTime =
            reformatDateString(timeElement[1].substring(start + 2),
                               "yyyy-MM-dd HH:mm:ss");

        start = timeElement[2].indexOf(":");

        int duration =
            Integer.parseInt(timeElement[2].substring(start + 2).split("\n")[0]);
        start = timeElement[1].indexOf(":");
        String endTime =
            reformatDateString(timeElement[1].substring(start + 2),
                               "yyyy-MM-dd HH:mm:ss",
                               duration);
        if (startorend.equalsIgnoreCase("end")) return endTime;
        else return startTime;
    }


    private String simpleTime (String s, String startorend)
    {
        int normalflag = 0;
        myRepeatPeriod = "none";
        int offset = 0;
        if (startorend.equalsIgnoreCase("end")) offset += 2;
        int start = s.indexOf(":");
        int end = s.indexOf("&");
        if (end == -1)
        {
            end = s.indexOf("<");
            normalflag = 1;
        }

        s = s.substring(start + 1, end);
        s = s.replaceAll(",", "");
        String[] timeElement = s.split(" ");
        if (normalflag == 0)
        {
            return timeElement[4] + "-" +
                   myMonth.get(timeElement[2].toLowerCase()) + "-" +
                   twoDigitNumber(timeElement[3]) + " " +
                   time(timeElement[5 + offset]);
        }
        else
        {
            return timeElement[4] +
                   "-" +
                   myMonth.get(timeElement[2].toLowerCase()) +
                   "-" +
                   twoDigitNumber(Integer.parseInt(timeElement[3]) + offset /
                                  2 + "") + " " + "00:00:00";
        }
    }


    private String twoDigitNumber (String number)
    {
        if (number.length() < 2) return "0" + number;
        return number;
    }


    private String time (String s)
    {
        String[] hourminute = new String[2];
        int offset = 0;
        if (s.contains("pm"))
        {
            offset += 12;
            s = s.replaceAll("pm", "");
        }
        else
        {
            s = s.replaceAll("am", "");
        }
        if (s.contains(":"))
        {
            String[] tmp = s.split(":");
            int hour = adjust(Integer.parseInt(tmp[0]) + offset);
            hourminute[0] = twoDigitNumber("" + hour);
            hourminute[1] = twoDigitNumber(tmp[1]);
        }
        else
        {
            int hour = adjust(Integer.parseInt(s) + offset);
            hourminute[0] = twoDigitNumber("" + hour);
            hourminute[1] = "00";
        }
        return hourminute[0] + ":" + hourminute[1] + ":00";
    }


    private int adjust (int i)
    {
        if (i == 12) return 0;
        if (i == 24) return 12;
        return i;
    }


    @SuppressWarnings("deprecation")
    public static String reformatDateString (String info,
                                             String oldFormat,
                                             int duration)
        throws ParseException
    {
        DateFormat df = new SimpleDateFormat(oldFormat);
        Date date = new Date();
        DateFormat eventFormat = new SimpleDateFormat(PropertyResourceBundle.getBundle("myProperties").getString("dateFormat"));

        date = df.parse(info);

        date.setSeconds(date.getSeconds() + duration);
        return eventFormat.format(date);
    }

    /**
     * Replaces createEvent in superclass because it has an extra field
     */
    @Override
    protected Event createEvent (Node currentEvent)
    {
        HashMap<String, String> fields = getMyFields(currentEvent);
        fields.put("repeat", getRepeatPeriod(currentEvent));

        return new Event(getMyFields(currentEvent));
    }
}
