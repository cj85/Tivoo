package filter;

import java.util.ArrayList;
import java.util.List;
import event.Event;


public class FilterByKeywordList extends FilterDecorator
{

    private List<String> myKeywordList;

    public final String myName="Key word in List: ";
    public FilterByKeywordList (ArrayList<String> keywordList)
    {
        super();
        myKeywordList =keywordList;
    }


    @Override
    public void filter (List<Event> list)
    {
        List<Event> decoratedList = decoratedFilterWork(list);
        for (Event entry : decoratedList)
        {
            for (String keyword:myKeywordList)
            {
                if (entry.containsKeywordInAllFields(keyword))
                {
                    myFilteredList.add(entry);
                    break;
                }
            }
        }
    }
    @Override
    public String getInformation() {
    // TODO Auto-generated method stub
    return myName+myKeywordList;
    }
}
