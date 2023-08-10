package in.co.mpwin.rebilling.jwt.service.impl;

import in.co.mpwin.rebilling.jwt.payload.LoginDto;
import in.co.mpwin.rebilling.jwt.repository.UserRepository;
import in.co.mpwin.rebilling.jwt.security.JwtTokenProvider;
import in.co.mpwin.rebilling.jwt.service.AuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    private JwtTokenProvider jwtTokenProvider;


    public AuthServiceImpl(AuthenticationManager authenticationManager, UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        // this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;

    }

    @Override
    public String login(LoginDto loginDto) {
        if (!userRepository.existsByUsername(loginDto.getUsername()))   {
            return "Username is not valid";
        }

        if(userRepository.findByUsername(loginDto.getUsername()).get().getStatus()=="inactive")   {
            return "User account is not active";
        }

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginDto.getUsername(),loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        return token;
    }

//    @Override
//    public String login2(String username) {
//
//        if (!userRepository.existsByUsername(username))   {
//            return "Username is not valid";
//        }
//        if(userRepository.findByUsername(username).get().getStatus()=="inactive")   {
//            return "User account is not active";
//        }
//
//        String token = jwtTokenProvider.generateToken(username);
//
//        return token;
//    }
}

