package com.online.buy.consumer.registration.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConsumerModel {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String password;

    private LocalDateTime registrationDate;

    private LocalDateTime lastLogin;

    private String status = "ACTIVE";

    private String role = "USER";

    private String tokenRefreshToken;

    private String profilePictureUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
