package in.co.mpwin.rebilling.jwt.service;

import in.co.mpwin.rebilling.jwt.entity.User;
import in.co.mpwin.rebilling.jwt.payload.UserDto;
import in.co.mpwin.rebilling.jwt.payload.UserResponse;
import jakarta.mail.MessagingException;

import java.io.UnsupportedEncodingException;

public interface UserService {

    UserResponse createUser(UserDto userDto);

    UserResponse updateUserDetails(UserDto userDto);

    UserDto getUserDtoByUsername(String username);

    UserResponse deactivateUser(String username,Boolean isActive);

    String updateResetPasswordToken(String token, String email);

    User getByResetPasswordToken(String token);

    void updatePassword(User user, String newPassword);

    void sendEmailForResetPassword(String recipientEmail, String link) throws MessagingException, UnsupportedEncodingException;

    void changePassword(String oldPassword, String newPassword);
}
