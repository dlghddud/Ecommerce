package com.example.ecommerce.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = "seat_id")
    }
)
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "seat_id", nullable = false)
    private Long seatId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReservationStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public enum ReservationStatus {
        PENDING,    // 결제 대기 (예약됨)
        CONFIRMED,  // 결제 완료 (예약 확정)
        CANCELED    // 취소됨
    }

    public Reservation(Long seatId, Long userId) {
        this.seatId = seatId;
        this.userId = userId;
        this.status = ReservationStatus.PENDING; // 생성 시 무조건 대기 상태
    }

    public void confirm() {
        this.status = ReservationStatus.CONFIRMED;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELED;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}