package com.online.buy.registration.processor.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegistratiionDto {
    private String username;
    private String email;
    private String password;
    private String role;  // "buyer", "seller", "admin"
}
