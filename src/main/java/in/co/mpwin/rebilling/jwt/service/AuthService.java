package in.co.mpwin.rebilling.jwt.service;

import in.co.mpwin.rebilling.jwt.payload.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
}
