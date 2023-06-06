package com.zerobase.yogizogi.oauth.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.yogizogi.oauth.domain.dto.KakaoProfile;
import com.zerobase.yogizogi.oauth.domain.dto.OAuthToken;
import com.zerobase.yogizogi.oauth.service.OAuthSignUpService;
import com.zerobase.yogizogi.user.common.UserRole;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
@RestController
public class OAuthController {
    private OAuthSignUpService oAuthSignUpService;
    @GetMapping("/oauth")
    public @ResponseBody String OAuthCallback(String code) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type",
            "application/x-www-form-urlencoded;charset=utf-8");
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "32665db00eb9aef9b6b5246fc2a2e8b4");
        params.add("redirect_uri", "http://localhost:8080/oauth");
        params.add("code", code);
        params.add("client_secret", "lj9RNptXDMYRrIOU9Bp6jpz45iCq3tXb");

        //HttpHeader 와 HttpBody 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params,
            httpHeaders);

        //Http 요청-Post 방식 - 그리고 response 변수의 응답 받음
        ResponseEntity<String> response = restTemplate.exchange(
            "https://kauth.kakao.com/oauth/token",
            HttpMethod.POST,
            kakaoTokenRequest,
            String.class
        );
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken = null;
        try {
            oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("카카오 엑세스 토큰 : " + oAuthToken.getAccess_token());

        RestTemplate restTemplate2 = new RestTemplate();
        HttpHeaders httpHeaders2 = new HttpHeaders();
        httpHeaders2.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
        httpHeaders.add("Content-type",
            "application/x-www-form-urlencoded;charset=utf-8");

        //HttpHeader 와 HttpBody 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(
            httpHeaders2);

        //Http 요청-Post 방식 - 그리고 response 변수의 응답 받음
        ResponseEntity<String> response2 = restTemplate2.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.POST,
            kakaoUserInfoRequest,
            String.class
        );
        System.out.println(response2.getBody());

        ObjectMapper objectMapper2 = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("카카오 아이디(번호) : " + kakaoProfile.getId());
        System.out.println("카카오 이메일 : " + kakaoProfile.getKakao_account().getEmail());

        //중복 방지를 위해
        System.out.println("카카오 가입한 유저 로그인 아이디 : " + kakaoProfile.getKakao_account().getEmail()
            + "_"
            + kakaoProfile.getId());
        String randomPassword = UUID.randomUUID().toString();

        AppUser user = AppUser.builder().sns(true)
            .email(kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId())
            .password(randomPassword).active(true).userRole(UserRole.USER)
            .emailAuthDateTime(LocalDateTime.now()).build();
        oAuthSignUpService.signUpOAuth(user);
        return response2.getBody();
    }
}
