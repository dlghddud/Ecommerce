package com.example.ecommerce.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private Long amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime createdAt;

    public enum PaymentStatus {
        READY,
        SUCCESS,
        FAIL
    }

    public Payment(Long orderId, Long amount) {
        this.orderId = orderId;
        this.amount = amount;
        this.status = PaymentStatus.READY;
        this.createdAt = LocalDateTime.now();
    }

    public void success() {
        this.status = PaymentStatus.SUCCESS;
    }

    public void fail() {
        this.status = PaymentStatus.FAIL;
    }
}