package com.example.ecommerce.controller;

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

    @PostMapping("/coupon/init")
    public String initCoupon(@RequestParam Long couponId,
                             @RequestParam int quantity) {

        redisTemplate.opsForValue()
                .set("coupon:stock:" + couponId, String.valueOf(quantity));

        return "쿠폰 재고 세팅 완료";
    }
}
