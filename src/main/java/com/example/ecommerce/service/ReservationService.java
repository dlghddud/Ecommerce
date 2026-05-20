package com.example.ecommerce.service;

import com.example.ecommerce.Entity.Coupon;
import com.example.ecommerce.Entity.CouponIssued;
import com.example.ecommerce.Entity.Reservation;
import com.example.ecommerce.Entity.Seat;
import com.example.ecommerce.repository.CouponIssuedRepository;
import com.example.ecommerce.repository.CouponRepository;
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
    private final OrderService orderService;
    private final CouponIssuedRepository couponIssuedRepository;
    private final CouponRepository couponRepository;

    @Transactional
    public String reserve(Long seatId, Long userId, Long couponIssuedId) {

        // 1. 좌석 조회 (가격 및 상태 확인용)
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 좌석"));

        if (seat.getStatus() != Seat.SeatStatus.AVAILABLE) {
            return "이미 예약되거나 판매된 좌석입니다.";
        }

        try {
            // 2. 예약 생성 (결제 대기 상태 PENDING)
            Reservation reservation = new Reservation(seat.getId(), userId);
            reservationRepository.save(reservation);

            // 3. 좌석 상태를 예약됨(RESERVED)으로 변경
            seat.reserve();
            seatRepository.save(seat);

            // 4. 쿠폰 할인 적용 계산
            Long finalAmount = seat.getPrice();
            
            if (couponIssuedId != null) {
                // 발급된 쿠폰 조회
                CouponIssued couponIssued = couponIssuedRepository.findById(couponIssuedId)
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 발급 쿠폰입니다."));
                
                // 쿠폰 소유자 확인 및 사용 여부 검증
                if (!couponIssued.getUserId().equals(userId)) {
                    throw new IllegalArgumentException("본인의 쿠폰만 사용할 수 있습니다.");
                }
                if (couponIssued.isUsed()) {
                    throw new IllegalArgumentException("이미 사용된 쿠폰입니다.");
                }
                
                // 원본 쿠폰 정보 조회하여 할인 금액 가져오기
                Coupon coupon = couponRepository.findById(couponIssued.getCouponId())
                        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠폰입니다."));
                
                Long discount = coupon.getDiscountAmount() != null ? coupon.getDiscountAmount() : 0L;
                
                // 할인 적용 (결제 금액이 0원 미만이 되지 않도록 방어 로직)
                finalAmount = Math.max(0, finalAmount - discount);
                
                // 쿠폰 사용 처리
                couponIssued.use();
                couponIssuedRepository.save(couponIssued);
            }

            // 5. 할인된 최종 가격으로 주문(Order) 생성
            Long orderId = orderService.createOrder(userId, reservation.getId(), finalAmount);

            if (couponIssuedId != null) {
                return "예약 성공 (쿠폰 적용됨). 주문 번호: " + orderId + ", 결제할 금액: " + finalAmount;
            } else {
                return "예약 성공. 주문 번호: " + orderId + ", 결제할 금액: " + finalAmount;
            }

        } catch (DataIntegrityViolationException e) {
            return "이미 예약된 좌석입니다.";
        }
    }
}