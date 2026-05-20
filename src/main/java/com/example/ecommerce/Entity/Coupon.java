package com.example.ecommerce.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Coupon {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private Integer totalQuantity;
  
  private Long discountAmount;

  public Coupon(String name, Integer totalQuantity, Long discountAmount) {
    this.name = name;
    this.totalQuantity = totalQuantity;
    this.discountAmount = discountAmount;
  }
  
  // 수량을 업데이트하는 메서드 추가
  public void updateTotalQuantity(int newQuantity) {
    this.totalQuantity = newQuantity;
  }
}