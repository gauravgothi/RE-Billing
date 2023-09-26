package in.co.mpwin.rebilling.jwt.controller;

import in.co.mpwin.rebilling.jwt.entity.User;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.jwt.payload.UserDto;
import in.co.mpwin.rebilling.jwt.payload.UserResponse;
import in.co.mpwin.rebilling.jwt.service.UserService;
import in.co.mpwin.rebilling.miscellanious.Message;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDto userDto)    {

        UserResponse userResponse = userService.createUser(userDto);
        return new ResponseEntity<>(userResponse.getMessage(),userResponse.getStatusCode());

    }

    @GetMapping("/get/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserDtoByUsername(@PathVariable("username") String username)  {
        UserDto userDto = userService.getUserDtoByUsername(username);
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserDetails(@Valid @RequestBody UserDto userDto)  {

        UserResponse userResponse = userService.updateUserDetails(userDto);
        return new ResponseEntity<>(userResponse.getMessage(),userResponse.getStatusCode());
    }

    @PutMapping("/active-inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactivateUser(@Valid @RequestBody UserDto userDto)  {

        UserResponse userResponse = userService.updateUserDetails(userDto);
        return new ResponseEntity<>(userResponse.getMessage(),userResponse.getStatusCode());
    }

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<?> processForgotPassword(HttpServletRequest request) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);
        ResponseEntity resp = null;
        try {
                String message = userService.updateResetPasswordToken(token, email);
                if (message.equals("success")) {
                    String resetPasswordLink = request.getRequestURL().toString().replace(request.getServletPath(), "")
                            + "/reset_password?token=" + token;
                    userService.sendEmailForResetPassword(email, resetPasswordLink);
                    resp = new ResponseEntity<>(new Message("We have sent a reset password link to your email. Please check."), HttpStatus.OK);
                }

        } catch (ApiException ex) {
            resp = new ResponseEntity<>(new Message(ex.getMessage()),HttpStatus.BAD_REQUEST);
        } catch (UnsupportedEncodingException | MessagingException e) {
            resp = new ResponseEntity<>(new Message("error : Error while sending email"),HttpStatus.BAD_REQUEST);
        } catch (Exception exception){
            resp = new ResponseEntity<>(new Message("Something went wrong"),HttpStatus.BAD_REQUEST);
        }
        return resp;
    }

    @GetMapping("/reset_password")
    public ResponseEntity<?> showResetPasswordForm(@Param(value = "token") String token) {
        try {
                User user = userService.getByResetPasswordToken(token);

                if (user == null)
                    return new ResponseEntity<>(new Message(token +" Invalid Token"),HttpStatus.BAD_REQUEST);

            }catch (Exception e){
            return new ResponseEntity<>(new Message("Something went wrong"),HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(new Message("success"),HttpStatus.OK);
    }

    @PostMapping("/reset_password")
    public ResponseEntity<?> processResetPassword(HttpServletRequest request) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        try {
                User user = userService.getByResetPasswordToken(token);

                if (user == null) {
                    return new ResponseEntity<>(new Message(token + " Invalid Token"), HttpStatus.BAD_REQUEST);
                } else {
                    userService.updatePassword(user, password);
                    return new ResponseEntity<>(new Message("You have successfully changed your password."), HttpStatus.OK);
                }
            }catch (Exception e){
                return new ResponseEntity<>(new Message("Something went wrong"),HttpStatus.BAD_REQUEST);
            }
    }

}

