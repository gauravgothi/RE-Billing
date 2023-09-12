package in.co.mpwin.rebilling.miscellanious;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
            dateList.add(previousReadDate);
            dateList.add(currentReadDate);

            return dateList;
        } catch (ParseException e) {
            throw e;
        }

    }
    public String getMonthYear(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        Date oneDayBefore = cal.getTime();
        return new SimpleDateFormat("MMM-yyyy").format(oneDayBefore);
    }

    public Date getOneDayBefore(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        Date oneDayBefore = cal.getTime();
        return oneDayBefore;
    }

    public List<String> getMonthsBetweenDates(String startMonth,String endMonth){
        List<String> monthList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-yyyy", Locale.ENGLISH);
        YearMonth startDate = YearMonth.parse(startMonth, formatter);
        YearMonth endDate = YearMonth.parse(endMonth, formatter);

        while(startDate.isBefore(endDate)) {
            monthList.add(startDate.format(formatter));
            startDate = startDate.plusMonths(1);
        }
        return monthList;
    }

}
