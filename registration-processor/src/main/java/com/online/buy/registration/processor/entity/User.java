package com.online.buy.registration.processor.entity;

import com.online.buy.registration.processor.enums.AccountStatus;
import com.online.buy.registration.processor.enums.Role;
import jakarta.persistence.*;
import lombok.*;

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

    private LocalDateTime createdAt = LocalDateTime.now();  // Record creation time

    private LocalDateTime updatedAt = LocalDateTime.now();  // Record update time

    @PreUpdate
    public void setUpdatedAt() {
        this.updatedAt = LocalDateTime.now();
    }
}
