package in.co.mpwin.rebilling.miscellanious;

import in.co.mpwin.rebilling.jwt.entity.User;
import in.co.mpwin.rebilling.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TokenInfo {
    @Autowired
    private UserService userService;
    public String getCurrentUsername()
    {
        //Get the Current Logged-In Username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }
    public String getCurrentUserRole()
    {
        //Get the Current Logged-In Username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username =  authentication.getName();
        return userService.getUserDtoByUsername(username).getRole();

    }
}
