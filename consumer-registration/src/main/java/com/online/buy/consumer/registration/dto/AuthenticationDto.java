package com.online.buy.consumer.registration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthenticationDto {

    @JsonProperty("username")
    @NotNull(message = "Username cannot be null")
    private String username;

    @JsonProperty("password")
    @NotNull(message = "Email cannot be null")
    private String password;
}
