package in.co.mpwin.rebilling.jwt.controller;

import in.co.mpwin.rebilling.jwt.entity.User;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.jwt.payload.UserDto;
import in.co.mpwin.rebilling.jwt.payload.UserResponse;
import in.co.mpwin.rebilling.jwt.service.UserService;
import in.co.mpwin.rebilling.miscellanious.Message;
import in.co.mpwin.rebilling.miscellanious.TokenInfo;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.internal.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createUser(@Valid @RequestBody UserDto userDto)    {

        final String methodName = "createUser() : ";
        logger.info(methodName + "called. with request body of userDto: {}",userDto);
        UserResponse userResponse = userService.createUser(userDto);
        logger.info(methodName + "return. success response with message : {}",userResponse.getMessage());
        return new ResponseEntity<>(userResponse.getMessage(),userResponse.getStatusCode());

    }

    @GetMapping("/get/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserDtoByUsername(@PathVariable("username") String username)  {
        final String methodName = "getUserDtoByUsername() : ";
        logger.info(methodName + "called. with parameters username: {}",username);
        UserDto userDto = userService.getUserDtoByUsername(username);
        logger.info(methodName + "return. userDto : {}",userDto.toString());
        return ResponseEntity.ok(userDto);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateUserDetails(@Valid @RequestBody UserDto userDto)  {

        final String methodName = "updateUserDetails() : ";
        logger.info(methodName + "called. with parameters updated userDto : {}",userDto);
        UserResponse userResponse = userService.updateUserDetails(userDto);
        logger.info(methodName + "return. success response with message : {}",userResponse.getMessage());
        return new ResponseEntity<>(userResponse.getMessage(),userResponse.getStatusCode());
    }

    @PutMapping("/active-inactive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deactivateUser(@Valid @RequestBody UserDto userDto)  {

        final String methodName = "deactivateUser() : ";
        logger.info(methodName + "called. with parameters updated userDto : {}",userDto);
        UserResponse userResponse = userService.updateUserDetails(userDto);
        logger.info(methodName + "return. success response with message : {}",userResponse.getMessage());
        return new ResponseEntity<>(userResponse.getMessage(),userResponse.getStatusCode());
    }

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        final String methodName = "showForgotPasswordForm() : ";
        logger.info(methodName + "called. with parameters empty :");
        logger.info(methodName + "return. forgot_password_form :");
        return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public ResponseEntity<?> processForgotPassword(HttpServletRequest request) {
        //HttpServletRequest request parameter of method in previous version
        final String methodName = "processForgotPassword() : ";
        logger.info(methodName + "called. with parameters HttpServletRequest request :{}",request);
//            String username = new TokenInfo().getCurrentUsername();
//            String email = userService.getUserDtoByUsername(username).getEmail();
//            if (email.equalsIgnoreCase(null))
//                throw new ApiException(HttpStatus.BAD_REQUEST,"email is not present for user "+ username);
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
            logger.info(methodName + "return. success after sending password reset link :{}",request);

        } catch (ApiException ex) {
            resp = new ResponseEntity<>(new Message(ex.getMessage()),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" API Exception occurred: {}", ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            resp = new ResponseEntity<>(new Message("error : Error while sending email"),HttpStatus.BAD_REQUEST);
            logger.error(methodName+"UnsupportedEncodingException OR MessagingException Exception occurred: {}", e.getMessage());
        } catch (Exception exception){
            resp = new ResponseEntity<>(new Message("Something went wrong"),HttpStatus.BAD_REQUEST);
            logger.error(methodName+" Exception occurred: {}", exception.getMessage(),exception);
        }
        return resp;
    }

    @GetMapping("/reset_password")
    public ResponseEntity<?> showResetPasswordForm(@Param(value = "token") String token) {
        final String methodName = "showResetPasswordForm() : ";
        logger.info(methodName + "called. with parameters String temp password token :{}",token);
        try {
                User user = userService.getByResetPasswordToken(token);

                if (user == null) {
                    logger.info(methodName + "return. invalid token response");
                    return new ResponseEntity<>(new Message(token + " Invalid Token"), HttpStatus.BAD_REQUEST);
                }

            }catch (Exception e){
            logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
            return new ResponseEntity<>(new Message("Something went wrong"),HttpStatus.BAD_REQUEST);
        }
        logger.info(methodName + "return. success after validation of token ");
        return new ResponseEntity<>(new Message("success"),HttpStatus.OK);
    }

    @PostMapping("/reset_password")
    public ResponseEntity<?> processResetPassword(HttpServletRequest request) {
        final String methodName = "processResetPassword() : ";
        logger.info(methodName + "called. with parameters HttpServletRequest request token:{}",request.getParameter("token"));
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        try {
                User user = userService.getByResetPasswordToken(token);

                if (user == null) {
                    logger.info(methodName + "return. invalid token response");
                    return new ResponseEntity<>(new Message(token + " Invalid Token"), HttpStatus.BAD_REQUEST);
                } else {
                    userService.updatePassword(user, password);
                    logger.info(methodName + "return. success of password change");
                    return new ResponseEntity<>(new Message("You have successfully changed your password."), HttpStatus.OK);
                }
            }catch (Exception e){
                logger.error(methodName+" Exception occurred: {}", e.getMessage(),e);
                return new ResponseEntity<>(new Message("Something went wrong"),HttpStatus.BAD_REQUEST);
            }
    }

}

