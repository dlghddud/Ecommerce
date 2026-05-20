package com.example.ecommerce.controller;

import com.example.ecommerce.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    // 예약 서비스에서 내부적으로 주문을 생성하므로, 
    // 직접 호출하는 POST /orders 대신 결제 API만 남겨두거나 구조를 변경할 수 있습니다.
    // 기존 테스트 등을 위해 유지하되, reservationId를 받도록 수정합니다.
    @PostMapping
    public Long createOrder(
            @RequestParam Long userId,
            @RequestParam Long reservationId,
            @RequestParam Long amount
    ) {
        return orderService.createOrder(userId, reservationId, amount);
    }

    @PostMapping("/{orderId}/pay")
    public String pay(@PathVariable Long orderId) {
        return orderService.pay(orderId);
    }
}