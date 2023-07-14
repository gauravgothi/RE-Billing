package in.co.mpwin.rebilling.miscellanious;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;

@Service
public class DateMethods {
    public Timestamp getServerTime()//used method
    {
        Calendar calendar = Calendar.getInstance();
        java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
        return ourJavaTimestampObject;
    }
}
