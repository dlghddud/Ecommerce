package com.example.ecommerce.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Entity
@Table(name = "seat")
@NoArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "concert_id")
    private Long concertId;

    @Column(name = "seat_number", length = 50)
    private String seatNumber;

    @Column(name = "price")
    private Long price = 10000L; // 기본 가격 설정

    @ColumnDefault("'AVAILABLE'")
    @Column(name = "status", length = 20)
    @Enumerated(EnumType.STRING)
    private SeatStatus status = SeatStatus.AVAILABLE;

    public enum SeatStatus {
        AVAILABLE,
        RESERVED,
        SOLD
    }
    
    public void reserve() {
        this.status = SeatStatus.RESERVED;
    }
    
    public void sold() {
        this.status = SeatStatus.SOLD;
    }
    
    public void cancel() {
        this.status = SeatStatus.AVAILABLE;
    }
}