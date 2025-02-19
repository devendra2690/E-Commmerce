package com.buy.it.authorization.server.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ClientRegisterRequest {
    private String clientId;
    private Set<String> redirectUris;
}
