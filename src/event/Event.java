package event;

import java.util.HashMap;
import exception.TivooEventKeywordNotFound;


public class Event
{

    private HashMap<String, String> myFields = new HashMap<String, String>();



    /**
     * Constructor of events with a map of fields to the information that the
     * field should have
     * 
     * @param map
     */
    public Event (HashMap<String, String> map)
    {
        myFields = map;
    }


    /**
     * Gets the information for a given field
     * 
     * @param key
     * @return
     */
    public String get (String key)
    {
        String toReturn = myFields.get(key);
        if (toReturn == null)
        {
            throw new TivooEventKeywordNotFound(String.format("%s is not found in field",
                                                              key));
        }
        else
        {
            return toReturn;
        }
    }


    /**
     * check if field has a given keyword
     * 
     * @param key
     * @param keyword
     * @return
     */
    public boolean containsKeyword (String key, String keyword)
    {
        return get(key).contains(keyword);
    }


    /**
     * checks if all fields have a keyword
     * 
     * @param keyword
     * @return
     */
    public boolean containsKeywordInAllFields (String keyword)
    {
        for (String s : myFields.values())
        {
            if (s != null && s.contains(keyword))
            {
                return true;
            }
        }
        return false;
    }

}
