package com.example.ecommerce.service;

import com.example.ecommerce.Entity.CouponIssued;
import com.example.ecommerce.repository.CouponIssuedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final StringRedisTemplate redisTemplate;
    private final RedisScript<Long> couponScript;
    private final CouponIssuedRepository couponIssuedRepository;

    public CouponIssued issueCoupon(Long couponId, Long userId) {

        String stockKey = "coupon:stock:" + couponId;
        String issuedKey = "coupon:issued:" + couponId + ":" + userId;

        Long result = redisTemplate.execute(
                couponScript,
                List.of(stockKey, issuedKey)
        );

        if (result == null) {
            throw new RuntimeException("Redis error");
        }
        if (result == -1) {
            throw new RuntimeException("이미 발급받은 쿠폰입니다.");
        }
        if (result == -2) {
            throw new RuntimeException("쿠폰이 모두 소진되었습니다.");
        }

        // 성공 → DB 저장
        try {
            return couponIssuedRepository.save(new CouponIssued(couponId, userId));
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("이미 발급된 쿠폰입니다 (DB)");
        }
    }
}
