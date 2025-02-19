package com.buy.it.authorization.server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistrationRequest {
    private String username;
    private String email;
    private String password;
    private String role;  // "buyer", "seller", "admin"
}
