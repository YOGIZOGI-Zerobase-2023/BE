package com.zerobase.yogizogi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
  //Security 를 모든 곳에서 허용하기 위한 설정.
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable();
    http.headers().frameOptions().sameOrigin(); // ex11을 위한 추가 설정 외부의 값을 받아오기 위한 설정
    http.authorizeRequests().anyRequest().permitAll();
  }
}