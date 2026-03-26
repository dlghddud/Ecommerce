package com.example.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                //csrf 끄기 (k6 테스트 때문에 필수)
                .csrf(csrf -> csrf.disable())
                //요청 권한 설정
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/coupons/**","/error").permitAll()
                        .requestMatchers("/admin/**").permitAll()
                        // 나머지는 인증 필요
                        .anyRequest().authenticated()
                )
                //테스트용이라 로그인도 끔
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());
        return http.build();
    }
}
