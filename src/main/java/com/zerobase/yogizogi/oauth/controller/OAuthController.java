package com.zerobase.yogizogi.oauth.controller;


import com.zerobase.yogizogi.oauth.service.OauthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Slf4j
@RestController
public class OAuthController {

    private final OauthService oauthService;

    @GetMapping("/oauth")
    public @ResponseBody String OAuthCallback(String code) {
        return oauthService.OAuthCallBack(code);
    }
}
