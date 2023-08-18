package in.co.mpwin.rebilling.jwt.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PasswordEncoderGenerator {

    public static void main(String[] args) throws ParseException {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11,new SecureRandom());
        System.out.println(passwordEncoder.encode("admin1234"));
        System.out.println(passwordEncoder.encode("HTBill123"));
        //Double a = 0.3,b=0.2;
        Double b = 25.4545787;
        System.out.println(BigDecimal.valueOf(b));
        BigDecimal a= BigDecimal.valueOf(-1.00000);
        BigDecimal d=new BigDecimal("0.2");
        System.out.println();
        System.out.println(a.subtract(d));
        Double e = 10.24548d;
        System.out.println(a.compareTo(BigDecimal.valueOf(2.000000)) );
        System.out.println("value= "+a);


        Date previousReadDate = new SimpleDateFormat("MMM-yyyy").parse("Feb-2023");
        System.out.println(previousReadDate);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(previousReadDate);
        calendar.add(calendar.MONTH,1);
        Date currentReadDate = calendar.getTime();
                System.out.println(currentReadDate);


    }
}
