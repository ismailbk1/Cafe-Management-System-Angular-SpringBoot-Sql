package com.inn.cafe.jwt;

import com.inn.cafe.dao.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApplicationConfig {


    private final UserDao userDao;

    @Bean
    public UserDetailsService userDetailsService(){
        return username -> userDao.findByEmailId(username);
    }
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider=new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    //This code sets up an AuthenticationManager Bean in a Spring Boot application.
    // It retrieves the AuthenticationManager from the AuthenticationConfiguration and makes it available
    // for use in the application context. This Bean is used to manage the authentication process in the
    // application,
    // using the UserDetailsService and PasswordEncoder defined in the application to validate user credentials.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
@Bean
    public PasswordEncoder passwordEncoder() {
     return new BCryptPasswordEncoder();
    }
}
