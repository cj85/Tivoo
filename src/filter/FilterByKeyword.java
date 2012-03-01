package filter;

import java.util.List;
import event.Event;
import exception.TivooEventKeywordNotFound;


public class FilterByKeyword extends FilterDecorator
{

    private String myKeyword;

    public final String myName="Fiter by Keyword: ";
    
    public String getInformation(){
// System.out.println(myName+myKeyword+"-----");/////////
     return myName+myKeyword;
    }
    public FilterByKeyword (String keyword)
    {
        super();
        myKeyword = keyword;
    }


    @Override
    public void filter (List<Event> list)
    {
        List<Event> decoratedList = decoratedFilterWork(list);
        for (Event entry : decoratedList)
        {
            try
            {
                if (entry.containsKeyword("title", myKeyword))
                {
                    myFilteredList.add(entry);
                }
            }
            catch (TivooEventKeywordNotFound e)
            {
                myFilteredList.add(entry);
            }
        }
    }
}
