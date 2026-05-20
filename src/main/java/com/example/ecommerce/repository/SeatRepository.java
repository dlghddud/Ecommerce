package com.example.ecommerce.repository;

import com.example.ecommerce.Entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
