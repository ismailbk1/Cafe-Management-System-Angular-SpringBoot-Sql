package com.inn.cafe.jwt;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig  {

    private final AuthenticationProvider authenticationProvider;
    private final JwtFilter JwtFilter;

@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/auth/register",
                        "/api/v1/auth/authenticate",
                        "/user/forgotPassword")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(JwtFilter, UsernamePasswordAuthenticationFilter.class);
http.cors().and();//// this very important The line http.cors().and();
// in your Spring Security configuration is responsible for enabling Cross-Origin Resource
// Sharing (CORS) support for your application. It allows you to configure how your
// application responds to
// requests coming from different origins (domains) to access your resources, such as APIs
        return http.build();
    }

}





