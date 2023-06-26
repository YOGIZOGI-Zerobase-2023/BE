package com.zerobase.yogizogi.global.config;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity  // 해당 애노테이션을 붙인 필터(현재 클래스)를 스프링 필터체인에 등록.
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {


    // 비밀번호 암호화 저장.
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // WebSecurity에 필터를 거는 게 훨씬 빠름. HttpSecrity에 필터를 걸면, 이미 스프링 시큐리티 내부에 들어온 상태기 때문에..
    @Override
    public void configure(WebSecurity web) throws Exception {
        //기본 페이지, users 와 swagger 페이지 있는 모든 곳은 시큐리티 적용 무시(이후 수정 예정. 모두 접근 가능 형태로 품 현재는)
        web.ignoring().mvcMatchers("/**", "/users/**", "/swagger-ui/**");
        // 정적인 리소스들에 대해서 시큐리티 적용 무시.
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//            .authorizeRequests()
//            .anyRequest()    // 모든 요청에 대해서 허용.
//            .permitAll()
//            .and()
//            .logout()
//            .logoutSuccessUrl("/")    // 로그아웃에 대해서 성공하면 "/"로 이동
//            .and()
//            .oauth2Login()
//            .defaultSuccessUrl("/login/success")
//            .userInfoEndpoint();
//        // oauth2 로그인에 성공하면, 유저 데이터를 가지고 우리가 생성한
//        // customOAuth2UserService에서 처리를 하겠다. 그리고 "/login-success"로 이동하라.
//    }
}