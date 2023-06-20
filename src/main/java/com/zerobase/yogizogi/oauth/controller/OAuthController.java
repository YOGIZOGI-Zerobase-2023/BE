package com.zerobase.yogizogi.oauth.controller;


import com.zerobase.yogizogi.global.ApiResponse;
import com.zerobase.yogizogi.oauth.service.OauthService;
import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        ApiResponse<?> apiResponse = oauthService.oAuthCallBack(code);
        if (apiResponse.getCode().equals("RESPONSE_SUCCESS")) {
            Map<String, Object> data = (Map<String, Object>) apiResponse.getData();
            if (data.containsKey("X-AUTH-TOKEN")) {
                String token = (String) data.get("X-Auth-key");
                String redirectUrl = FRONTEND_URL + "/?token=" + token;
                response.sendRedirect(redirectUrl);
            }
        } else {
            // Handle error case
            // For example, return an error response or redirect to an error page
            response.sendRedirect(FRONTEND_URL + "/error");
        }
    }
}
