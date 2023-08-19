package com.inn.cafe.auth;

import com.inn.cafe.dao.UserDao;
import com.inn.cafe.jwt.JwtService;
import com.inn.cafe.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {


    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest request) {
         var user= User.builder()
                 .name(request.getName())
                 .email(request.getEmail())
                 .contactNumber(request.getContactNumber())
                 .password(passwordEncoder.encode(request.getPassword()))
                 .role("user")
                 .status("false")


                 .build();

         userDao.save(user);
         var jwttoken=jwtService.genrateTokenWithRole(user.getEmail(),user.getRole());
         return AuthenticationResponse.builder().token(jwttoken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )


            )  ;
        }catch(Exception e){
            e.printStackTrace();
        }
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(
                           request.getEmail(),
                           request.getPassword()
                   )


           )  ;
           // if the password and the username is correct then we will get the user from the data base and create a token
           log.info("before the find user function");
           var user=userDao.findByEmailId(request.getEmail())
                   ;
        var jwttoken=jwtService.genrateTokenWithRole(user.getEmail(),user.getRole());
        return AuthenticationResponse.builder().token(jwttoken).build();


    }
}
