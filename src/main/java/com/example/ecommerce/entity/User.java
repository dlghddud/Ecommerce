package com.example.ecommerce.entity;

import lombok.Getter;
import lombok.Setter;
import java.security.Timestamp;

@Getter
@Setter
public class User {
  private long id;
  private String email;
  private String password;
  private String nickname;
  private String status;
  private Timestamp createdAt;
  private Timestamp updatedAt;
}