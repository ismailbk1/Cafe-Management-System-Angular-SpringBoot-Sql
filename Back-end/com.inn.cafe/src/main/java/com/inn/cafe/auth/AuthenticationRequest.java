package com.inn.cafe.auth;

import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class AuthenticationRequest {

    private String email;
    String password;
}
