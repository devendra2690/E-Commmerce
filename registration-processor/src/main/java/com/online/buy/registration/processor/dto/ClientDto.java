package com.online.buy.registration.processor.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ClientDto {

    private Long id;

    @NotNull(message = "Client ID cannot be null")
    private String clientId;

    @Size(min = 1, message = "Redirect URIs cannot be empty")
    private Set<String> redirectUris;

    @NotNull(message = "Client name cannot be null")
    private String name;

    @NotNull(message = "Client email cannot be null")
    private String email;

    @NotNull(message = "Client secret cannot be null")
    private String clientSecret;
}
