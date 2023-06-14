package com.zerobase.yogizogi.oauth.service;

import com.zerobase.yogizogi.global.ApiResponse;
import com.zerobase.yogizogi.global.ResponseCode;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.token.JwtAuthenticationProvider;
import java.util.Map;
import java.util.TreeMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {

    private final JwtAuthenticationProvider provider;

    public ApiResponse<?> oAuthLogin(AppUser user) {

        String[] emails = user.getEmail().split("_");

        Map<String, Object> data = new TreeMap<>();
        data.put("X-AUTH-TOKEN", provider.createToken(user.getEmail(), user.getId()));
        data.put("email", emails[0]);
        data.put("nickname", user.getNickName());

        return new ApiResponse<>(
            ResponseCode.RESPONSE_SUCCESS.getCode(),
            HttpStatus.OK,
            "SUCCESS",
            data
        );
    }
}
