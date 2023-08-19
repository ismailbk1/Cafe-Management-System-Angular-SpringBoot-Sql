package com.inn.cafe.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
     Claims claims=null;
     private final JwtService jwtService;
    private final CostomerUserDetailsService userDetailsService;
    private String username;
    @Override
    protected void doFilterInternal(
             HttpServletRequest request,
             HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException
    {
            final String jwt;

            final String authHeader=request.getHeader("Authorization");
        log.info(authHeader);

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
log.info(authHeader);
                filterChain.doFilter(request, response);

                return;


            } else {

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
log.info(authHeader);
                    jwt = authHeader.substring(7);
                   claims=jwtService.extractAllClaims(jwt);
                    username = jwtService.extractUSername(jwt);
                    // username existe and the user not yet connected
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                        if (jwtService.isTokenValid(jwt, userDetails)) {
                            log.info("all somthing work will in the filter ");
                            UsernamePasswordAuthenticationToken authToken =
                                    new UsernamePasswordAuthenticationToken(userDetails,
                                            null,
                                            userDetails.getAuthorities());
                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        }
                    }
                    filterChain.doFilter(request, response);

                }
            }

    }

    public  boolean isAdmin(){
        return "admin".equalsIgnoreCase((String) claims.get("role"));
    }
    public  boolean isUser(){
        return "user".equalsIgnoreCase((String) claims.get("role"));
    }
    public String getCurrentUser(){
        return username;
    }
}
