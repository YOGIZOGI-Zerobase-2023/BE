package com.zerobase.yogizogi.oauth.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.yogizogi.oauth.domain.model.OAuthToken;
import com.zerobase.yogizogi.oauth.service.OauthService;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("user")
public class OAuthController {

    private final OauthService oauthService;
    @Value("${frontend.url}")
    private String FRONTEND_URL;

    @GetMapping("/kakao-login")
    public void oAuthCallback(String code, HttpServletResponse response) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-type",
            "application/x-www-form-urlencoded;charset=utf-8");
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "32665db00eb9aef9b6b5246fc2a2e8b4");
        params.add("redirect_uri", "http://localhost:8080/user/kakao-login");
        params.add("code", code);
        params.add("client_secret", "lj9RNptXDMYRrIOU9Bp6jpz45iCq3tXb");

        //HttpHeader 와 HttpBody 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params,
            httpHeaders);

        //Http 요청-Post 방식 - 그리고 response 변수의 응답 받음
        ResponseEntity<String> res = restTemplate.exchange(
            "https://kauth.kakao.com/oauth/token",
            HttpMethod.POST,
            kakaoTokenRequest,
            String.class
        );
        ObjectMapper objectMapper = new ObjectMapper();
        OAuthToken oAuthToken;
        try {
            oAuthToken = objectMapper.readValue(res.getBody(), OAuthToken.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        //System.out.println("카카오 엑세스 토큰 : " + oAuthToken.getAccess_token());

        String token = oauthService.kakaoProfileCheck(oAuthToken);
            if (!token.isEmpty()) {
                String redirectUrl = FRONTEND_URL + "?token=" + token;
                response.sendRedirect(redirectUrl);
            }
             else {
                response.sendRedirect(FRONTEND_URL + "/error");
            }
    }
}
