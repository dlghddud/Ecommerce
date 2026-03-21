package com.example.ecommerce.Entity;

import lombok.Getter;
import lombok.Setter;

import java.security.Timestamp;

@Getter
@Setter
public class Coupon {
  private long id;
  private String name;
  private long totalQuantity;
  private Timestamp createdAt;

}
