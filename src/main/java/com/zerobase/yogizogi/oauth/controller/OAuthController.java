package com.zerobase.yogizogi.oauth.controller;


import com.zerobase.yogizogi.global.ApiResponse;
import com.zerobase.yogizogi.oauth.service.OauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("user")
public class OAuthController {

    private final OauthService oauthService;

    @GetMapping("/kakao-login")
    public @ResponseBody ApiResponse<?> oAuthCallback(String code) {
        return oauthService.oAuthCallBack(code);
    }
}
