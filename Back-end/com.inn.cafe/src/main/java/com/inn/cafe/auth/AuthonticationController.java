package com.inn.cafe.auth;

import com.inn.cafe.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200") // Replace with the origin of your Angular app

@RequestMapping("/api/v1/auth")
public class AuthonticationController {
private  final AuthenticationService service;
@Autowired
    JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(

            @RequestBody RegisterRequest request){
        log.info("the request i send {}",request.getContactNumber());
        log.info("in the register fonction ");

        return ResponseEntity.ok(service.register(request));

    }
        @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse>  authenticat(
            @RequestBody AuthenticationRequest request
    )
    {log.info("in the auth function");
        return ResponseEntity.ok(service.authenticate(request));
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authorizationHeader) {
        // Assuming the header is in the format: "Bearer <token>"
        String token = authorizationHeader.substring("Bearer ".length());

        // Revoke the token
        jwtService.revokeToken(token);

        return ResponseEntity.ok("Logged out successfully");
    }
}
