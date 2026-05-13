package com.example.ecommerce.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "coupon_issued",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_coupon_user", columnNames = {"coupon_id", "user_id"})
        }
)
@Getter
@NoArgsConstructor
public class CouponIssued {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "coupon_id", nullable = false)
    private Long couponId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public CouponIssued(Long couponId, Long userId) {
        this.couponId = couponId;
        this.userId = userId;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
