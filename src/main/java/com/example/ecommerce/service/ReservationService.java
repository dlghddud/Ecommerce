package com.example.ecommerce.service;

import com.example.ecommerce.Entity.Reservation;
import com.example.ecommerce.repository.ReservationRepository;
import com.example.ecommerce.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final SeatRepository seatRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public String reserve(Long seatId, Long userId) {

        seatRepository.findById(seatId)
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 좌석"));

        try {

            reservationRepository.save(
                    new Reservation(seatId, userId)
            );

            return "예약 성공";

        } catch (DataIntegrityViolationException e) {

            return "이미 예약된 좌석입니다.";
        }
    }
}
