package in.co.mpwin.rebilling.jwt.service.impl;

import in.co.mpwin.rebilling.jwt.controller.UserController;
import in.co.mpwin.rebilling.jwt.entity.User;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.jwt.payload.UserDto;
import in.co.mpwin.rebilling.jwt.payload.UserResponse;
import in.co.mpwin.rebilling.jwt.repository.UserRepository;
import in.co.mpwin.rebilling.jwt.service.UserService;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import in.co.mpwin.rebilling.miscellanious.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
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

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private ModelMapper modelMapper;


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserResponse createUser(UserDto userDto) {
        final String methodName = "createUser() : ";
        logger.info(methodName + "called. with request body of userDto: {}",userDto);
        try {
            if (userRepository.existsByUsername(userDto.getUsername())) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Username is already exist");
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
        } catch (ApiException ex) {
            logger.error(methodName + "throw API Exception.");
            throw ex;
        } catch (DataIntegrityViolationException e) {
            logger.error(methodName + "throw DataIntegrityViolationException Exception");
            throw e;
        } catch (Exception exception) {
            logger.error(methodName + "throw common Exception");
            throw exception;
        }
    }



    @Override
    public UserResponse updateUserDetails(UserDto userDto) {

        final String methodName = "updateUserDetails() : ";
        logger.info(methodName + "called. with parameters updated userDto : {}",userDto);
        try {
            User user = userRepository.findByUsername(userDto.getUsername())
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, userDto.getUsername() + "Username is not found"));

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
        }catch (ApiException ex) {
            logger.error(methodName + "throw API Exception.");
            throw ex;
        } catch (DataIntegrityViolationException e) {
            logger.error(methodName + "throw DataIntegrityViolationException Exception");
            throw e;
        } catch (Exception exception) {
            logger.error(methodName + "throw common Exception");
            throw exception;
        }

    }

    @Override
    public UserDto getUserDtoByUsername(String username) {

        User savedUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, username + "Username is not found"));

        UserDto userDto = modelMapper.map(savedUser, UserDto.class);

        return userDto;
    }

    @Override
    public UserResponse deactivateUser(String username, Boolean isActive) {

        final String methodName = "deactivateUser() : ";
        logger.info(methodName + "called. with parameters updated username : {}, isActive : {}",username,isActive);

        try {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST, username + "Username is not found"));

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
        }catch (ApiException ex) {
            logger.error(methodName + "throw API Exception.");
            throw ex;
        } catch (DataIntegrityViolationException e) {
            logger.error(methodName + "throw DataIntegrityViolationException Exception");
            throw e;
        } catch (Exception exception) {
            logger.error(methodName + "throw common Exception");
            throw exception;
        }

    }

    public String updateResetPasswordToken(String token, String email) throws ApiException {
        final String methodName = "updateResetPasswordToken() : ";
        logger.info(methodName + "called. with parameters updated token : {}, email : {}",token,email);
        try {
                User user = userRepository.findByEmail(email);
                if (user != null) {
                    user.setResetPasswordToken(token);
                    userRepository.save(user);
                    return "success";
                } else
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Could not find any user with the email " + email);

        }catch (ApiException ex) {
            logger.error(methodName + "throw API Exception.");
            throw ex;
        } catch (DataIntegrityViolationException e) {
            logger.error(methodName + "throw DataIntegrityViolationException Exception");
            throw e;
        } catch (Exception exception) {
            logger.error(methodName + "throw common Exception");
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

    @Transactional
    public void changePassword(String oldPassword, String newPassword) {
        final String methodName = "changePassword() : ";
        logger.info(methodName + "called. with parameters updated oldPassword and newPassword.");
        try {
            //Get the Current Logged-In Username
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,"user "+ username + " is not valid."));

            //encode the user provided old password for comparison
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11, new SecureRandom());

            boolean isOldSameAsProvided = passwordEncoder.matches(oldPassword,user.getPassword());

            if (isOldSameAsProvided){
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
            }else
                throw new ApiException(HttpStatus.BAD_REQUEST,"Old password is wrong.");
        }catch (ApiException ex) {
            logger.error(methodName + "throw API Exception.");
            throw ex;
        } catch (DataIntegrityViolationException e) {
            logger.error(methodName + "throw DataIntegrityViolationException Exception");
            throw e;
        } catch (Exception exception) {
            logger.error(methodName + "throw common Exception");
            throw exception;
        }

    }
}
