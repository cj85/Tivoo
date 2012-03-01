import java.util.Enumeration;
import java.util.ResourceBundle;


public class LocalResourceBundle extends ResourceBundle
{

    @Override
    public Enumeration<String> getKeys ()
    {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    protected Object handleGetObject (String key)
    {
        if(key.equals("dateFormat")) return "yyyy-MM-dd HH:mm:ss";
        return null;
    }

}
