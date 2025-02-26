package com.buy.it.authorization.server.entity;

import com.buy.it.authorization.server.enums.AccountStatus;
import com.buy.it.authorization.server.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    private UUID id;  // Unique user identifier

    @Column(unique = true, nullable = false, length = 50)
    private String username;  // Unique username

    @Column(unique = true, nullable = false, length = 100)
    private String email;  // Email for login

    @Column(nullable = false)
    private String passwordHash;  // Hashed password

    private String phoneNumber;  // Optional phone number

    private String profilePicture;  // URL to profile picture

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;  // Role (buyer, seller, admin)

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> permissions;  // User permissions

    private LocalDateTime lastLogin;  // Last login time

    private int failedAttempts;  // Failed login attempts

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.ACTIVE;  // Account status

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;  // Record creation time

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;  // Record update time

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false) // Foreign key to Client table
    private Client client;
}
