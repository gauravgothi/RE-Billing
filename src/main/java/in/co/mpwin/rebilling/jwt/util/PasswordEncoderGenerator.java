package in.co.mpwin.rebilling.jwt.util;

import in.co.mpwin.rebilling.miscellanious.DateMethods;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class PasswordEncoderGenerator {

    public static void main(String[] args) throws ParseException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11, new SecureRandom());
        System.out.println(passwordEncoder.encode("abc1234"));
        System.out.println(passwordEncoder.encode("HTBill123"));
        //Double a = 0.3,b=0.2;
        Double b = 25.4545787;
//        System.out.println(BigDecimal.valueOf(b));
//        BigDecimal a= BigDecimal.valueOf(-1.00000);
//        BigDecimal d=new BigDecimal("0.2");
//        System.out.println();
//        System.out.println(a.subtract(d));
//        Double e = 10.24548d;
//        System.out.println(a.compareTo(BigDecimal.valueOf(2.000000)) );
//        System.out.println("value= "+a);


//        Calendar calendar = Calendar.getInstance();
//        System.out.println(calendar);
//        System.out.println(calendar.getTime());
//        java.sql.Timestamp ourJavaTimestampObject = new java.sql.Timestamp(calendar.getTime().getTime());
//        System.out.println(ourJavaTimestampObject);

//        Date previousReadDate = new SimpleDateFormat("MMM-yyyy").parse("Feb-2023");
//        System.out.println(previousReadDate);
//
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(new Date());
        gc.set( Calendar.HOUR_OF_DAY, 0 );
        gc.set( Calendar.MINUTE, 0 );
        gc.set( Calendar.SECOND, 0 );
        gc.set( Calendar.MILLISECOND, 0 );
        System.out.println(gc.getTime());

        //List<BigDecimal> bigDecimalList = List.of(5,10,15)

//        double d=-.00012;
//        System.out.println(d+""); //This prints -1.2E-4
//
//        double c=47.48d;
//        BigDecimal z = new BigDecimal(47.48);
//        System.out.println(z);
//        //This prints 47.47999999999999687361196265555918216705322265625
//
//        calendar = Calendar.getInstance();
//        java.sql.Timestamp ourJavaTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());
//        System.out.println(ourJavaTimestamp);;



//        Date date = new SimpleDateFormat("dd-MM-yyyy").parse("01-10-2023");
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        cal.add(Calendar.DATE, -1);
//        Date oneDayBefore = cal.getTime();
//        System.out.println(new SimpleDateFormat("MMM-yyyy").format(oneDayBefore));
//
//        LinkedHashSet<String> gfg = new LinkedHashSet<String>();
//        // Adding element to LinkedHashSet
//        gfg.add("Geeks");
//        gfg.add("for");
//        gfg.add("geeks");
//        LinkedHashSet<String> newgfg = new LinkedHashSet<>(gfg);
//        int initialSetSize,finalSetSize;
//
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

/*
        Date date = new SimpleDateFormat("dd-MM-yyyy").parse("01-10-2023");

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, -1);
        Date oneDayBefore = cal.getTime();
        System.out.println(new SimpleDateFormat("MMM-yyyy").format(oneDayBefore));





        // Create a new LinkedHashSet to store elements to add
        //LinkedHashSet<String> elementsToAdd = new LinkedHashSet<String>(gfg);


//        do {
//            gfg = new LinkedHashSet<>(newgfg);
//            initialSetSize = newgfg.size();
//            // Iterating LinkedHashSet using enhanced for loop
//            for (String itr : gfg) {
//                if (itr.equals("for")) {
//                    newgfg.add("from");
//                }
//            }
//
//            finalSetSize = newgfg.size();
//        }while (initialSetSize != finalSetSize);
//
//        // Now, print the modified LinkedHashSet
//        for (String itr : newgfg) {
//            System.out.println(itr);
//        }

        do {
            gfg = new LinkedHashSet<>(newgfg);
            initialSetSize = newgfg.size();
            // Iterating LinkedHashSet using enhanced for loop
            for (String itr : gfg) {
                if (itr.equals("for")) {
                    newgfg.add("from");
                }
            }

            finalSetSize = newgfg.size();
        }while (initialSetSize != finalSetSize);

        // Now, print the modified LinkedHashSet
        for (String itr : newgfg) {
            System.out.println(itr);
        }*/

        Timestamp replacementDate = new DateMethods().getServerTime();
        LocalDate endDate = LocalDate.now();
        System.out.println("local end date ="+endDate);


/*
        try{
            String msg = null;
            System.out.println("string msg ="+msg+" and length="+msg.length()+" and lower case="+msg.toLowerCase());
        }catch(NullPointerException ex)
        {
            System.out.println("ex.getMessage() ="+ex.getMessage());
            System.out.println(" \n ex.getLocalizedMessage() ="+ex.getLocalizedMessage());
            System.out.println(" \n ex.toString() ="+ex.toString());
            System.out.println(" \n ex.getCause() ="+ex.getCause());
            System.out.println(" \n ex.getStackTrace()="+ex.getStackTrace());

        }*/

    }
}
