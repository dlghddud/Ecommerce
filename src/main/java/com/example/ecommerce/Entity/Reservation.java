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

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public Reservation(Long seatId, Long userId) {
        this.seatId = seatId;
        this.userId = userId;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

}