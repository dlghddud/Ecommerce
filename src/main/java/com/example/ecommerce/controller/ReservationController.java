package com.example.ecommerce.controller;

import com.example.ecommerce.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<String> reserve(
            @RequestParam Long seatId,
            @RequestParam Long userId,
            // 쿠폰을 적용하기 위해 파라미터 추가 (선택적)
            @RequestParam(required = false) Long couponIssuedId
    ) {

        return ResponseEntity.ok(
                reservationService.reserve(seatId, userId, couponIssuedId)
        );
    }
}