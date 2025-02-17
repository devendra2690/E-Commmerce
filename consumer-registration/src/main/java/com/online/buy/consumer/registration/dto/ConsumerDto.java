package com.online.buy.consumer.registration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConsumerDto {

    @NotNull(message = "First name can not be null")
    @JsonProperty("first_name")
    private String firstName;

    @NotNull(message = "Last name can not be null")
    @JsonProperty("last_name")
    private String lastName;

    @Email
    @JsonProperty("email")
    private String email;

    @NotNull
    @JsonProperty("phone")
    private String phoneNumber;

    @JsonProperty("password")
    @NotNull(message = "Email cannot be null")
    private String password;
}
