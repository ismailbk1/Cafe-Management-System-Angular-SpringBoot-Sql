package com.inn.cafe.auth;

import lombok.*;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder

public class AuthenticationResponse {
    private String token;
}
