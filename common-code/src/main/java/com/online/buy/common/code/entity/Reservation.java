package com.online.buy.common.code.entity;

import com.online.buy.common.code.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"createdAt","userId", "productId", "quantity", "status"})
})
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id",nullable = false)
    private Long productId;

    @Column(name = "user_id",nullable = false)
    private String userId;

    @Column(name = "quantity",nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status",nullable = false)
    private ReservationStatus status;

    @CreationTimestamp
    @Column(name = "created_at",updatable = false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
