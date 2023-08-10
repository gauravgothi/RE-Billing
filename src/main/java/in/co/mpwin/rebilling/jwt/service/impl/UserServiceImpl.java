package in.co.mpwin.rebilling.jwt.service.impl;

import in.co.mpwin.rebilling.jwt.entity.User;
import in.co.mpwin.rebilling.jwt.exception.ApiException;
import in.co.mpwin.rebilling.jwt.payload.UserDto;
import in.co.mpwin.rebilling.jwt.payload.UserResponse;
import in.co.mpwin.rebilling.jwt.repository.UserRepository;
import in.co.mpwin.rebilling.jwt.service.UserService;
import in.co.mpwin.rebilling.miscellanious.DateMethods;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;



    @Override
    public UserResponse createUser(UserDto userDto) {

        if(userRepository.existsByUsername(userDto.getUsername()))      {
            return new UserResponse("Username is already exist", HttpStatus.BAD_REQUEST);
        }

        //Set<String> usernameSetInRoles = userDto.getRoles().stream().map(Role::getUsername).collect(Collectors.toSet());
//        if (!usernameSetInRoles.stream().allMatch(s -> s.equals(userDto.getUsername())))    {
//            return new UserResponse("Username in roles must be same as username of User!..",HttpStatus.BAD_REQUEST);
//        }

        //change password to encoded form
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(11,new SecureRandom());
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));

        User user = modelMapper.map(userDto, User.class);

        //Get the Current Logged-In Username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        //set above current user
        user.setCreatedBy(currentPrincipalName);
        user.setCreatedOn(new DateMethods().getServerTime());

        userRepository.save(user);

        return new UserResponse("User created successfully!...",HttpStatus.CREATED);
    }

    @Override
    public UserResponse updateUserDetails(UserDto userDto) {

        User user = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,userDto.getUsername() + "Username is not found"));

//        user.setOfficeEmail(userDto.getOfficeEmail());
//        user.setUserRemark(user.getUserRemark());

        userRepository.save(user);
        //Get the Current Logged-In Username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        //set above current user
        user.setUpdatedBy(currentPrincipalName);
        user.setUpdatedOn(new DateMethods().getServerTime());

        return new UserResponse("User Details updated successfully",HttpStatus.OK);
    }

    @Override
    public UserDto getUserDtoByUsername(String username) {

        User savedUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,username + "Username is not found"));

        UserDto userDto = modelMapper.map(savedUser,UserDto.class);

        return userDto;
    }

    @Override
    public UserResponse deactivateUser(String username,Boolean isActive) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND,username + "Username is not found"));

//        user.setIsActive(isActive);
//        user.setUserRemark(user.getUserRemark());

        userRepository.save(user);

        //Get the Current Logged-In Username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        //set above current user
        user.setUpdatedBy(currentPrincipalName);
        user.setUpdatedOn(new  DateMethods().getServerTime());

        return new UserResponse("User Activation status changed successfully",HttpStatus.OK);
    }
}
