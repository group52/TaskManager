package model;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/** class DateTimeAdapter  converts Date for JAX */
public class DateTimeAdapter extends XmlAdapter<String,Date> {
    private final DateFormat dateFormat =  new SimpleDateFormat("yyyy.MM.dd HH:mm");

    /** Unmarshal string
     @return date at acceptable format
     @param   xml */
    @Override
    public Date unmarshal(String xml) throws Exception {
        return dateFormat.parse(xml);

    }
    /** Marshal date
     @return String from date
     @param   object */
    @Override
    public String marshal(Date object) throws Exception {
        return dateFormat.format(object);
    }
}
