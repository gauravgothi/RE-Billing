package in.co.mpwin.rebilling.miscellanious;

import in.co.mpwin.rebilling.beans.readingbean.MeterReadingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ValidatorService {
    public String removeSpaceFromString(String data)
    {
        return data.replace(" ","");
    }


}
