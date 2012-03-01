package filter;

import java.util.List;
import event.Event;


public abstract class Filter
{
    protected List<Event> myOriginalList, myFilteredList;

    /**
     * Filters the input event list. 
     * @param list
     */
    public abstract void filter (List<Event> list);

    /**
     * Returns the filtered event list. 
     * @return
     */
    public List<Event> getFilteredList ()
    {
        return myFilteredList;
    }

    /**
     * Returns the unfiltered event list. 
     * @return
     */
    public List<Event> getOriginalList ()
    {
        return myOriginalList;
    }
    public abstract String getInformation();
}
