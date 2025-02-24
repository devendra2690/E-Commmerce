package com.online.buy.registration.processor.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ClientModel {

    private Long id;
    private String name;
    private String email;
    private List<ProductModel> productModels;
    private Set<String> redirectUris;
    private String clientSecret;
    private String clientId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
