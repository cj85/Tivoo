package parser;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import event.Event;
import exception.TivooInvalidFeed;
import exception.TivooSystemError;
import exception.TivooUnrecognizedFeed;


public abstract class Parser
{
    protected ArrayList<Event> myEvents;
    protected NodeList myEventList;
    protected ResourceBundle myResources;

    /**
     * Constructor initializes myEvents and myEventList
     */
    public Parser ()
    {
        myEvents = new ArrayList<Event>();
        myEventList = null;
    }


    /**
     * loads a file and creates a node list stores the list in myEventList
     * throws an exception if the file can not be loaded
     * 
     * @param file
     */
    public void loadFile (File file)
    {
        Document document = generateDocument(file);
        NodeList nodeList = getTagNodes(document, getHead());
        if (nodeList.getLength() == 0)
        {
            throw new TivooUnrecognizedFeed();
        }
        myEventList = nodeList;
    }


    /**
     * Template method for finding the head node
     * 
     * @return
     */
    protected abstract String getHead ();


    /**
     * loops through the entire node list creating an event and adding the event
     * to the list myEvents
     */
    public void parse ()
    {
        if (myEventList == null)
        {
            return;
        }
        for (int temp = 0; temp < myEventList.getLength(); temp++)
        {
            Node currentEvent = myEventList.item(temp);

            myEvents.add(createEvent(currentEvent));
        }
    }


    /**
     * from a given node creates an Event
     * 
     * @param currentEvent
     * @return Event
     */
    protected Event createEvent (Node currentEvent)
    {
        return new Event(getMyFields(currentEvent));
    }


    /**
     * gets the 5 main fields of an event
     * 
     * @param currentEvent
     * @return HashMap<String,String>
     */
    protected HashMap<String, String> getMyFields (Node currentEvent)
    {
        HashMap<String, String> fields = new HashMap<String, String>();
        fields.put("title", getTitle(currentEvent));
        fields.put("summary", getSummary(currentEvent));
        fields.put("url", getURL(currentEvent));
        fields.put("startTime", getStartDate(currentEvent));
        fields.put("endTime", getEndDate(currentEvent));
        return fields;
    }


    /**
     * Allows for generic events and reduces code duplication so the parser has
     * default behavior
     * 
     * @param currentEvent
     * @return
     */

    protected String getTitle (Node currentEvent)
    {

        return null;
    }


    /**
     * Allows for generic events and reduces code duplication so the parser has
     * default behavior
     * 
     * @param currentEvent
     * @return
     */

    protected String getSummary (Node currentEvent)
    {
        return null;
    }


    /**
     * Allows for generic events and reduces code duplication so the parser has
     * default behavior
     * 
     * @param currentEvent
     * @return
     */

    protected String getURL (Node currentEvent)
    {
        return null;
    }


    /**
     * Allows for generic events and reduces code duplication so the parser has
     * default behavior
     * 
     * @param currentEvent
     * @return
     */

    protected String getStartDate (Node currentEvent)
    {
        return null;
    }


    /**
     * Allows for generic events and reduces code duplication so the parser has
     * default behavior
     * 
     * @param currentEvent
     * @return
     */

    protected String getEndDate (Node currentEvent)
    {
        return null;
    }


    /**
     * returns the eventList
     * 
     * @return myEvents
     */

    public List<Event> getEventList ()
    {
        return new ArrayList<Event>(myEvents);
    }


    /**
     * creates a Document from a file
     * 
     * @param file
     * @return Document
     */
    private Document generateDocument (File file)
    {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        Document toReturn = null;
        try
        {
            dBuilder = dbFactory.newDocumentBuilder();
            toReturn = dBuilder.parse(file);
            toReturn.getDocumentElement().normalize();
        }
        catch (ParserConfigurationException e)
        {
            throw new TivooSystemError("DocumentBuilderFactory initialization failed");
        }
        catch (SAXException e)
        {
            throw new TivooInvalidFeed();
        }
        catch (IOException e)
        {
            throw new TivooInvalidFeed();
        }
        return toReturn;
    }


    /**
     * returns a NodeList of the subnodes of a Node
     * 
     * @param Node
     * @param xPath
     * @return
     */
    protected NodeList getTagNodes (Object Node, String xPath)
    {
        XPathExpression expr = getXPathExpression(xPath);
        NodeList nodeList;
        try
        {
            nodeList = (NodeList) expr.evaluate(Node, XPathConstants.NODESET);
        }
        catch (XPathExpressionException e)
        {
            throw new TivooSystemError("getTagNodes failed");
        }
        return nodeList;
    }


    /**
     * gets the tagvalue of a node
     * 
     * @param Node
     * @param xPath
     * @return
     */
    protected String getTagValue (Object Node, String xPath)
    {
        Node node = getTagNodes(Node, xPath).item(0);
        if (node == null)
        {
            return "";
        }
        else
        {
            return node.getNodeValue();
        }
    }


    /**
     * creates Xpaths
     * 
     * @param xPath
     * @return
     */
    private XPathExpression getXPathExpression (String xPath)
    {
        XPathFactory xpathfactory = XPathFactory.newInstance();
        XPath myXpath = xpathfactory.newXPath();
        XPathExpression toReturn;
        try
        {
            toReturn = myXpath.compile(xPath);
        }
        catch (XPathExpressionException e)
        {
            throw new TivooSystemError("xPath complication failed");
        }
        return toReturn;
    }


    /**
     * Takes in a string and an oldformat and transforms the date string into a
     * lexigraphical one
     * 
     * @param info
     * @param oldFormat
     * @return
     */
    protected String reformatDateString (String info, String oldFormat)
    {
        DateFormat df = new SimpleDateFormat(oldFormat);
        Date date = new Date();
        DateFormat eventFormat = new SimpleDateFormat(PropertyResourceBundle.getBundle("myProperties").getString("dateFormat"));
        try
        {
            date = df.parse(info);
        }
        catch (ParseException e)
        {
            System.out.println(info);

            System.out.println(oldFormat);
            e.printStackTrace();
            throw new TivooSystemError("reformatDateString failed");
        }
        return eventFormat.format(date);
    }

}
