package com.example.ecommerce.repository;

import com.example.ecommerce.Entity.CouponIssued;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CouponIssuedRepository extends JpaRepository<CouponIssued, Long> {

    // 이미 발급 받았는지 확인 (fallback or 검증용)
    boolean existsByCouponIdAndUserId(Long couponId, Long userId);

    // 특정 쿠폰 발급 수 조회 (통계 or 검증용)
    long countByCouponId(Long couponId);

    // 특정 유저가 받은 쿠폰 목록
    List<CouponIssued> findByUserId(Long userId);
}
