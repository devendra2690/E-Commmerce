package com.buy.it.authorization.server.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "oauth2_client")
public class OAuth2Client {
    @Id
    private String id;

    @Column(nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String clientSecret;

    @Column(nullable = false)
    private String clientAuthenticationMethods;

    @Column(nullable = false)
    private String authorizationGrantTypes;

    @Column
    private String redirectUris;

    @Column(nullable = false)
    private String scopes;

    @Column
    private Boolean requireAuthorizationConsent;
}