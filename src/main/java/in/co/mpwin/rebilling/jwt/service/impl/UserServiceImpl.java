package in.co.mpwin.rebilling.jwt.service.impl;

import in.co.mpwin.rebilling.jwt.entity.User;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.jwt.payload.UserDto;
import in.co.mpwin.rebilling.jwt.payload.UserResponse;
import in.co.mpwin.rebilling.jwt.repository.UserRepository;
import in.co.mpwin.rebilling.jwt.service.UserService;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserResponse createUser(UserDto userDto) {

        if (userRepository.existsByUsername(userDto.getUsername())) {
            return new UserResponse("Username is already exist", HttpStatus.BAD_REQUEST);
        }

        //Set<String> usernameSetInRoles = userDto.getRoles().stream().map(Role::getUsername).collect(Collectors.toSet());
//        if (!usernameSetInRoles.stream().allMatch(s -> s.equals(userDto.getUsername())))    {
//            return new UserResponse("Username in roles must be same as username of User!..",HttpStatus.BAD_REQUEST);
//        }

        //change password to encoded form
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11, new SecureRandom());
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User user = modelMapper.map(userDto, User.class);

        //Get the Current Logged-In Username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        //set above current user
        user.setCreatedBy(currentPrincipalName);
        user.setCreatedOn(new DateMethods().getServerTime());

        userRepository.save(user);

        return new UserResponse("User created successfully!...", HttpStatus.CREATED);
    }

    @Override
    public UserResponse updateUserDetails(UserDto userDto) {

        User user = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, userDto.getUsername() + "Username is not found"));

//        user.setOfficeEmail(userDto.getOfficeEmail());
//        user.setUserRemark(user.getUserRemark());

        userRepository.save(user);
        //Get the Current Logged-In Username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        //set above current user
        user.setUpdatedBy(currentPrincipalName);
        user.setUpdatedOn(new DateMethods().getServerTime());

        return new UserResponse("User Details updated successfully", HttpStatus.OK);
    }

    @Override
    public UserDto getUserDtoByUsername(String username) {

        User savedUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, username + "Username is not found"));

        UserDto userDto = modelMapper.map(savedUser, UserDto.class);

        return userDto;
    }

    @Override
    public UserResponse deactivateUser(String username, Boolean isActive) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, username + "Username is not found"));

//        user.setIsActive(isActive);
//        user.setUserRemark(user.getUserRemark());

        userRepository.save(user);

        //Get the Current Logged-In Username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        //set above current user
        user.setUpdatedBy(currentPrincipalName);
        user.setUpdatedOn(new DateMethods().getServerTime());

        return new UserResponse("User Activation status changed successfully", HttpStatus.OK);
    }

    public String updateResetPasswordToken(String token, String email) throws ApiException {
        try {
                User user = userRepository.findByEmail(email);
                if (user != null) {
                    user.setResetPasswordToken(token);
                    userRepository.save(user);
                    return "success";
                } else
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Could not find any user with the email " + email);

        }catch (ApiException apiException){
            throw apiException;
        }catch (Exception exception){
            throw exception;
        }

    }

    public User getByResetPasswordToken(String token) {
        try {
            User user = userRepository.findByResetPasswordToken(token);
            return user;
        }catch (Exception e){
            throw e;
        }
    }

    public void updatePassword(User user, String newPassword) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11, new SecureRandom());
        user.setPassword(passwordEncoder.encode(newPassword));
        try {
            user.setResetPasswordToken(null);
            userRepository.save(user);
        }catch (Exception e){
            throw e;
        }


    }

    public void sendEmailForResetPassword(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        {
            MimeMessageHelper helper = new MimeMessageHelper(message);

            helper.setFrom("gauravgothi.mppkvvcl@gmail.com", "RE Billing Support");
            helper.setTo(recipientEmail);

            String subject = "Here's the link to reset your password";

            String content = "<p>Hello,</p>"
                    + "<p>You have requested to reset your password.</p>"
                    + "<p>Click the link below to change your password:</p>"
                    + "<p><a href=\"" + link + "\">Change my password</a></p>"
                    + "<br>"
                    + "<p>Ignore this email if you do remember your password, "
                    + "or you have not made the request.</p>";

            helper.setSubject(subject);

            helper.setText(content, true);

            mailSender.send(message);
        }
    }
}
