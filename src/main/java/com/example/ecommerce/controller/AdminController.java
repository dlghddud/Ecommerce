package com.example.ecommerce.controller;

import com.example.ecommerce.Entity.Coupon;
import com.example.ecommerce.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final StringRedisTemplate redisTemplate;
    private final CouponRepository couponRepository;

    @PostMapping("/coupon/init")
    public String initCoupon(@RequestParam Long couponId,
                             @RequestParam int quantity) {

        // 1. DB의 Coupon 엔티티 수량 업데이트
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));
        
        coupon.updateTotalQuantity(quantity);
        couponRepository.save(coupon);

        // 2. Redis의 재고 수량 업데이트 (캐시 워밍)
        redisTemplate.opsForValue()
                .set("coupon:stock:" + couponId, String.valueOf(quantity));

        return "쿠폰 재고 세팅 완료 (DB 및 Redis 모두 " + quantity + "개로 동기화됨)";
    }
}
