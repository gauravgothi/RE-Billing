package in.co.mpwin.rebilling.miscellanious;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class DateMethods {
    public Timestamp getServerTime()//used method
    {
        Calendar calendar = Calendar.getInstance();
        java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
        return ourJavaTimestampObject;
    }

    public List<Date> getCurrentAndPreviousDate(String month) throws ParseException {
        //get previous read date from month eg Feb-2023 to 01-02-2023
        Date previousReadDate = null;
        try {
            previousReadDate = new SimpleDateFormat("MMM-yyyy").parse(month);
            //get current read date from month eg Feb-2023 to 01-03-2023
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(previousReadDate);
            calendar.add(calendar.MONTH,1);
            Date currentReadDate = calendar.getTime();

            List<Date> dateList = new ArrayList<>();
            dateList.add(currentReadDate);
            dateList.add(previousReadDate);

            return dateList;
        } catch (ParseException e) {
            throw e;
        }

    }

}
