import javax.swing.JFrame;
import exception.TivooException;


public class Main
{ public static final String TITLE = "TivooViewer";
    public static void main (String[] args)
    {
        TivooSystem model = new TivooSystem();
        try
        {
        	TivooViewer display = new TivooViewer(model);
        	// create container that will work with Window manager
        	JFrame frame = new JFrame(TITLE);
        	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	// add our user interface components to Frame and show it
        	frame.getContentPane().add(display);
        	frame.pack();
        	frame.setVisible(true);
        	
 /*           File file1 = new File("./xml/dukecal.xml");
            File file1 = new File("./xml/dukecal.xml");
            File file2 = new File("./xml/DukeBasketBall.xml");
            File file3 = new File("./xml/TVTest.xml");
            File file4 = new File("./xml/googlecal.xml");
            File file5 = new File("./xml/NFL.xml");
            
			// model.loadFile(file1);
            model.loadFile(file2);
            model.loadFile(file3);
            model.loadFile(file4);
            model.loadFile(file5);
			
//            model.addFilterByKeyword("vs");
            model.addFilterByTimeFrame("2011-07-21 18:30:00", "2012-01-21 13:00:00");

            String[] s = {"NFL", "Duke", "ACM", "Exhibition", "Meet"};
            model.addFilterByKeywordList(s);
            model.addFilterByKeywordSorting("title");
			
            model.addSummaryAndDetailPagesWriter("./html/summary.html");
			
            model.addConflictWriter("./html/conflicts.html");
			
            model.addListWriter("./html/listview.html");
			
            model.addCalendarWriter("./html/calendarview.html", "2011-11-21 18:30:00", "MONTH");
			
            model.perform();
*/

        }
        catch (TivooException e)
        {
			
            System.err.println(e.getMessage());
        }
    }
}
