package com.buy.it.authorization.server.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String clientSecret; // Hashed using BCrypt

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> grantTypes; // E.g., "authorization_code", "client_credentials"

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> redirectUris; // Required for authorization_code grant

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> scopes; // E.g., "read", "write"
}
