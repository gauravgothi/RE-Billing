package in.co.mpwin.rebilling.miscellanious;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String getCurrentUsername()
    {
        //Get the Current Logged-In Username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
}
