package com.zerobase.yogizogi.oauth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.yogizogi.oauth.domain.model.OAuthToken;
import com.zerobase.yogizogi.oauth.service.OauthService;
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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("user")
public class OAuthController {

    private final OauthService oauthService;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${frontend.url}")
    private String frontendUrl;

    @Value("${kakao.redirect.url}")
    private String kakaoRedirectUrl;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.client.secret}")
    private String clientSecret;

    @GetMapping("/kakao-login")
    public void oAuthCallback(String code, HttpServletResponse response) throws IOException {
        OAuthToken oAuthToken = getOAuthToken(code);
        String token = oauthService.kakaoProfileCheck(oAuthToken);

        if (!token.isEmpty()) {
            response.sendRedirect(frontendUrl + "?token=" + token);
        } else {
            response.sendRedirect(frontendUrl + "/error");
        }
    }

    private OAuthToken getOAuthToken(String code) {
        ResponseEntity<String> res = restTemplate.exchange(
            "https://kauth.kakao.com/oauth/token",
            HttpMethod.POST,
            new HttpEntity<>(createTokenRequestParams(code), createTokenRequestHeaders()),
            String.class
        );

        try {
            return objectMapper.readValue(res.getBody(), OAuthToken.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpHeaders createTokenRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        return headers;
    }

    private MultiValueMap<String, String> createTokenRequestParams(String code) {
        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", kakaoRedirectUrl);
        params.add("code", code);
        params.add("client_secret", clientSecret);
        return params;
    }
}

