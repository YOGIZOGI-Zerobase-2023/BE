package com.zerobase.yogizogi.oauth.controller;


import com.zerobase.yogizogi.oauth.service.OauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("user")
public class OAuthController {

    private final OauthService oauthService;

    @GetMapping("/kakao-login")
    public ResponseEntity<?> oAuthCallback(String code) {
        return ResponseEntity.ok(oauthService.oAuthCallBack(code));
    }
}
