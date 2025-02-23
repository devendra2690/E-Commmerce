package com.online.buy.consumer.registration.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ConsumerModel {

    private Long consumerId;

    private String firstName;

    private String lastName;

    private String email;

    @JsonIgnore
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

    private List<AddressModel> addressModelList;
}
