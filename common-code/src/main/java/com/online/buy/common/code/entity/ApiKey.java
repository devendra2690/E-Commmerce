package com.online.buy.common.code.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "api_keys")
@Getter @Setter
public class ApiKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String clientName;

    @Column(unique = true, nullable = false)
    private String apiKey;

    private int rateLimit;

    private boolean isActive = true;
}
