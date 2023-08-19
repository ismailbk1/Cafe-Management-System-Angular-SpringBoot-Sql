package com.inn.cafe.auth;

import lombok.*;

@Data

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String name;
    private String contactNumber;
    private String email;
    private String password;



}
