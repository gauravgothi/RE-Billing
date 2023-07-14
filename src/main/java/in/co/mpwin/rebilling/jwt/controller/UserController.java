package in.co.mpwin.rebilling.jwt.controller;

import in.co.mpwin.rebilling.jwt.payload.UserDto;
import in.co.mpwin.rebilling.jwt.payload.UserResponse;
import in.co.mpwin.rebilling.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

}

