package com.zerobase.yogizogi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .authorizeRequests()
            .antMatchers("/console/**").permitAll() // H2 콘솔 접근 허용
            .antMatchers("/").permitAll() // 루트 경로 접근 허용
            .anyRequest().authenticated() // 나머지 요청은 인증 필요
            .and()
            .formLogin()
            .permitAll()
            .and()
            .csrf()
            .disable();
        // H2 콘솔 관련 설정 추가
        httpSecurity.headers().frameOptions().disable();
    }
}

//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
//
//  private final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;
//  private final UserDetailsService userDetailsService;
//
//  // 사용자 정의 필터 등록
//  @Bean
//  public UserTypeFilter userTypeFilter() throws Exception {
//    UserTypeFilter filter = new UserTypeFilter();
//    filter.setAuthenticationManager(authenticationManager());
//    return filter;
//  }
//
//  // 사용자 정의 필터 설정
//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//    http
//        .addFilterBefore(userTypeFilter(), UsernamePasswordAuthenticationFilter.class)
//        .authorizeRequests()
//        .antMatchers("/oauth/**").permitAll() // OAuth 로그인 필요 없음
//        .antMatchers("/login/**").permitAll() // 일반 로그인 필요 없음
//        .anyRequest().authenticated()
//        .and()
//        .oauth2Login()
//        .userInfoEndpoint()
//        .userService(oAuth2UserService)
//        .and()
//        .formLogin();
//  }
//
//  // User의 인증 매니저 구성
//  @Override
//  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//    auth
//        .userDetailsService(userDetailsService)
//        .passwordEncoder(passwordEncoder());
//  }
//
//  // Password Encoder 설정
//  @Bean
//  public PasswordEncoder passwordEncoder() {
//    return new BCryptPasswordEncoder();
//  }
//}