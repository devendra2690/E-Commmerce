package com.buy.it.authorization.server.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "oauth2_clients")
@Data
public class OAuth2Client {

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

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
