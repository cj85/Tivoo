package parser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.PropertyResourceBundle;
import org.w3c.dom.Node;

public class DukeBasketballParser extends Parser {
	private final String myOldFormat = PropertyResourceBundle.getBundle(
			"myProperties_en").getString("dukeballFormat");
	private static String myEventDateFormat = PropertyResourceBundle.getBundle(
			"myProperties_en").getString("dateFormat");

	@Override
	protected String getHead() {
		return "/dataroot/Calendar";
	}

	protected String getTitle(Node currentEvent) {
		return getTagValue(currentEvent, "Subject/text()");
	}

	protected String getSummary(Node currentEvent) {
		return getTagValue(currentEvent, "Description/text()");
	}

	protected String getURL(Node currentEvent) {
		String summary = getSummary(currentEvent);
		int index = summary.indexOf("http://");
		return summary.substring(index);
	}

	protected String getStartDate(Node currentEvent) {
		String startDate = getTagValue(currentEvent, "StartDate/text()");
		String startTime = getTagValue(currentEvent, "StartTime/text()");
		String info = startDate + " " + startTime;
		return adjust(info);
	}

	protected String getEndDate(Node currentEvent) {
		String endDate = getTagValue(currentEvent, "EndDate/text()");
		String endTime = getTagValue(currentEvent, "EndTime/text()");
		String info = endDate + " " + endTime;
		return adjust(info);
	}

	private String adjust(String time) {
		int offset = 0;
		if (time.toLowerCase().endsWith("pm"))
			offset += 12;
		return reformatDateString(time, myOldFormat, offset);

	}

	@SuppressWarnings("deprecation")
	private static String reformatDateString(String info, String oldFormat,
			int duration) {
		DateFormat df = new SimpleDateFormat(oldFormat);
		Date date = new Date();
		DateFormat eventFormat = new SimpleDateFormat(myEventDateFormat);
		try {
			date = df.parse(info);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		date.setHours(date.getHours() + duration);
		return eventFormat.format(date);
	}

}
