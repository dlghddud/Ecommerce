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

    @ColumnDefault("'AVAILABLE'")
    @Column(name = "status", length = 20)
    @Enumerated(EnumType.STRING)
    private String status;

}