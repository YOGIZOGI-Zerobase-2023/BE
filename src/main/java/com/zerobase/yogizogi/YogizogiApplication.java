package com.zerobase.yogizogi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
@EntityScan(basePackages = {"com.zerobase.yogizogi"})
public class YogizogiApplication {

  public static void main(String[] args) {
    SpringApplication.run(YogizogiApplication.class, args);
  }

}
