package in.co.mpwin.rebilling.jwt.controller;

import in.co.mpwin.rebilling.jwt.payload.JwtAuthResponse;
import in.co.mpwin.rebilling.jwt.payload.LoginDto;
import in.co.mpwin.rebilling.jwt.service.AuthService;
import in.co.mpwin.rebilling.miscellanious.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
            return new ResponseEntity<>(new Message(response), HttpStatus.BAD_REQUEST);
        }

        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setAccessToken(response);

        return ResponseEntity.ok(jwtAuthResponse);
    }

//    @PostMapping("/login2/username/{username}")
//    public ResponseEntity<?> login2(@PathVariable("username") String username)  {
//
//
//
//        String response = authService.login2(username);
//        if (response.equals("Username is not valid") || response.equals("User account is not active"))  {
//            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//        }
//
//        JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
//        jwtAuthResponse.setAccessToken(response);
//
//        return ResponseEntity.ok(jwtAuthResponse);
//    }
}
