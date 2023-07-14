package in.co.mpwin.rebilling.jwt.service;

import in.co.mpwin.rebilling.jwt.payload.UserDto;
import in.co.mpwin.rebilling.jwt.payload.UserResponse;

public interface UserService {

    UserResponse createUser(UserDto userDto);

    UserResponse updateUserDetails(UserDto userDto);

    UserDto getUserDtoByUsername(String username);

    UserResponse deactivateUser(String username,Boolean isActive);

}
