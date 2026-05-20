package com.example.ecommerce.service;

import com.example.ecommerce.Entity.Order;
import com.example.ecommerce.Entity.Payment;
import com.example.ecommerce.Entity.Reservation;
import com.example.ecommerce.Entity.Seat;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.PaymentRepository;
import com.example.ecommerce.repository.ReservationRepository;
import com.example.ecommerce.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final SeatRepository seatRepository;

    @Transactional
    public Long createOrder(Long userId, Long reservationId, Long amount) {
        Order order = new Order(userId, reservationId, amount);
        orderRepository.save(order);
        return order.getId();
    }

    @Transactional
    public String pay(Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문"));

        Payment payment = new Payment(order.getId(), order.getTotalAmount());

        try {
            boolean paymentResult = fakePaymentApi();

            if (!paymentResult) {
                throw new RuntimeException("결제 API 호출 실패");
            }

            // 1. 결제 성공 처리
            payment.success();
            order.paid();
            paymentRepository.save(payment);

            // 2. 연결된 예약(Reservation) 상태를 확정(CONFIRMED)으로 변경
            Reservation reservation = reservationRepository.findById(order.getReservationId())
                    .orElseThrow(() -> new IllegalArgumentException("예약 정보를 찾을 수 없습니다."));
            reservation.confirm();
            reservationRepository.save(reservation);

            // 3. 좌석(Seat) 상태를 판매완료(SOLD)로 변경
            Seat seat = seatRepository.findById(reservation.getSeatId())
                    .orElseThrow(() -> new IllegalArgumentException("좌석 정보를 찾을 수 없습니다."));
            seat.sold();
            seatRepository.save(seat);

            return "결제 성공 및 예약 확정";

        } catch (Exception e) {

            // 결제 실패 시 처리
            payment.fail();
            order.cancel();
            paymentRepository.save(payment);
            
            // 결제 실패 시 예약 취소 및 좌석 해제
            Reservation reservation = reservationRepository.findById(order.getReservationId()).orElse(null);
            if(reservation != null) {
                reservation.cancel();
                reservationRepository.save(reservation);
                
                Seat seat = seatRepository.findById(reservation.getSeatId()).orElse(null);
                if(seat != null) {
                    seat.cancel(); // 좌석을 다시 AVAILABLE로 변경
                    seatRepository.save(seat);
                }
                
                // 참고: 실무에서는 결제 실패 시 사용했던 쿠폰을 복구해 주는 로직이 여기에 추가됩니다.
                // (couponIssued.restore() 호출 등)
            }

            return "결제 실패로 인한 예약 취소";
        }
    }
    
    private boolean fakePaymentApi() {
        return true; 
    }
}