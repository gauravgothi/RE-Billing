package in.co.mpwin.rebilling.miscellanious;

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
