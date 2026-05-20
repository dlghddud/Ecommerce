package com.example.ecommerce.controller;

import com.example.ecommerce.Entity.CouponIssued;
import com.example.ecommerce.service.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    @PostMapping("/{couponId}/issue")
    public ResponseEntity<CouponIssued> issue(
            @PathVariable Long couponId,
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(
                couponService.issueCoupon(couponId, userId)
        );
    }
}
