package in.co.mpwin.rebilling.jwt.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;

public class PasswordEncoderGenerator {

    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11,new SecureRandom());
        System.out.println(passwordEncoder.encode("admin1234"));
        System.out.println(passwordEncoder.encode("HTBill123"));

    }
}
