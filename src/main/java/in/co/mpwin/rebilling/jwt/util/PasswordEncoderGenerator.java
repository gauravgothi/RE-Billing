package in.co.mpwin.rebilling.jwt.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.text.DecimalFormat;

public class PasswordEncoderGenerator {

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11,new SecureRandom());
        System.out.println(passwordEncoder.encode("admin1234"));
        System.out.println(passwordEncoder.encode("HTBill123"));
        //Double a = 0.3,b=0.2;
        Double b = 25.4545787;
        System.out.println(BigDecimal.valueOf(b));
        BigDecimal a=new BigDecimal("0.35");
        BigDecimal d=new BigDecimal("0.2");
        System.out.println();
        System.out.println(a.subtract(d));
        Double e = 10.24548d;
        System.out.println(new DecimalFormat("#.000000").format(e));

    }
}
