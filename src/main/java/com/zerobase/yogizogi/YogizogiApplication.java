package com.zerobase.yogizogi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class YogizogiApplication {

  /**
   * 비밀번호 보완을 위한 BcyptoPasswordEncoder() 설정
   */
  @Bean
  public BCryptPasswordEncoder encoder(){
    return new BCryptPasswordEncoder();
  }
  public static void main(String[] args) {
    SpringApplication.run(YogizogiApplication.class, args);
  }

}
