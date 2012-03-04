package writer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.hp.gagawa.java.elements.A;
import com.hp.gagawa.java.elements.Body;
import com.hp.gagawa.java.elements.H1;
import com.hp.gagawa.java.elements.Head;
import com.hp.gagawa.java.elements.Html;
import com.hp.gagawa.java.elements.Table;
import com.hp.gagawa.java.elements.Td;
import com.hp.gagawa.java.elements.Text;
import com.hp.gagawa.java.elements.Title;
import com.hp.gagawa.java.elements.Tr;
import event.Event;
import exception.TivooInvalidOutputDirectory;


public abstract class Writer
{

    private String myTitle, myDirectory;


    /**
     * Writes a given Gagawa HTML object to a file specified by filename.
     * @param html
     * @param filename
     */
    protected void write (Html html, String filename)
    {
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            bw.write(html.write());
            bw.flush();
            bw.close();
        }
        catch (IOException e)
        {
            throw new TivooInvalidOutputDirectory();
        }
    }

    /**
     * Initialize HTML object by setting up the page with a page title.
     * @return
     */
    protected ArrayList<Object> initializeHTMLDocument ()
    {
    	ArrayList<Object> toReturn=new ArrayList<Object>();
        Html html = new Html();
        Head head = new Head();

        html.appendChild(head);

        Title title = new Title();
        title.appendChild(new Text(myTitle + " Page"));
        head.appendChild(title);

        Body body = new Body();

        html.appendChild(body);

        H1 h1 = new H1();
        h1.appendChild(new Text(myTitle));
        body.appendChild(h1);
        toReturn.add(html);
        toReturn.add(body);
        return toReturn;
    }

    /**
     * Outputs html to a specified file location.
     * @param events
     */
    abstract public void outputHTML (List<Event> events);

    /**
     * Get the title of the writer.
     * @return
     */
    protected String getMyTitle ()
    {
        return myTitle;
    }

    /**
     * Set the title of the writer.
     * @return
     */
    protected void setMyTitle (String myTitle)
    {
        this.myTitle = myTitle;
    }

    /**
     * Get the directory of the writer that it is writing to.
     * @return
     */
    protected String getMyDirectory ()
    {
        return myDirectory;
    }


    protected void setMyDirectory (String myDirectory)
    {
        this.myDirectory = myDirectory;
    }


    /**
     * Adds a row  to a table.
     * @param details
     * @param eventType
     * @param detail
     */
    protected void generateDetail (Table details,
                                   String eventType,
                                   String detail)
    {
        String fullDetail = eventType + ": " + detail;
        details.appendChild(new Tr().appendChild(new Td().appendChild(new Text(fullDetail))));
    }

    /**
     * Adds an event to a table as a row with title, start and end time. 
     * @param events
     * @param table
     * @param event
     */
    protected void addEventToTable (List<Event> events, Table table, Event event)
    {
        Tr event_format = new Tr();
        A link = new A();

        String linkText = "event" + events.indexOf(event) + ".html";
        link.setHref(linkText);
        link.appendChild(new Text(event.get("title")));

        event_format.appendChild((new Td()).appendChild(link));
        event_format.appendChild((new Td()).appendChild(new Text(event.get("startTime")
                                                                      .toString())));
        event_format.appendChild((new Td()).appendChild(new Text(event.get("endTime")
                                                                      .toString())));

        table.appendChild(event_format);
    }
    public  abstract String getName();
}
