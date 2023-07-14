package in.co.mpwin.rebilling.jwt.controller;

import in.co.mpwin.rebilling.jwt.payload.JwtAuthResponse;
import in.co.mpwin.rebilling.jwt.payload.LoginDto;
import in.co.mpwin.rebilling.jwt.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //Login Rest API
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto)  {

        String response = authService.login(loginDto);
        if (response.equals("Username is not valid") || response.equals("User account is not active"))  {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(response);

        return ResponseEntity.ok(jwtAuthResponse);
    }
}
