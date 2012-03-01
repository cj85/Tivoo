package filter;

import java.util.ArrayList;
import java.util.List;
import event.Event;


public abstract class FilterDecorator extends Filter
{
    protected Filter myDecoratedFilter;


    public FilterDecorator ()
    {
        myOriginalList = new ArrayList<Event>();
        myFilteredList = new ArrayList<Event>();
    }

    /**
     * Lets the decorated filter work first. 
     * @param list
     * @return
     */
    protected List<Event> decoratedFilterWork (List<Event> list)
    {
        myOriginalList = list;
        myFilteredList = new ArrayList<Event>();
        if (myDecoratedFilter == null)
        {
            return list;
        }
        else
        {
            myDecoratedFilter.filter(list);
            return myDecoratedFilter.getFilteredList();
        }
    }

    /**
     * Appends a new filter to the current one. 
     * @param filter
     */
    public void appendFilter (Filter filter)
    {
        myDecoratedFilter = filter;
    }
    public boolean hasNext(){
        return myDecoratedFilter!=null;
       }
       public FilterDecorator next(){
        return (FilterDecorator) myDecoratedFilter;
       }

}
