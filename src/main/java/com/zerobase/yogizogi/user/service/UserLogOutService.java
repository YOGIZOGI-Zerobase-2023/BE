package com.zerobase.yogizogi.user.service;

import com.zerobase.yogizogi.global.ApiResponse;
import com.zerobase.yogizogi.global.ResponseCode;
import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import com.zerobase.yogizogi.user.token.JwtAuthenticationProvider;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLogOutService {

    private final JwtAuthenticationProvider provider;

    public ApiResponse<?> logout(HttpServletRequest request) {
        String all = request.getHeader("Authorization");
        if (all == null || !all.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }
        String token = all.substring(7);//토큰 가져오기.
        if(provider.validateToken(token)){
            throw new CustomException(ErrorCode.DO_NOT_ALLOW_TOKEN);
        }

        return new ApiResponse<>(
            ResponseCode.RESPONSE_SUCCESS.getCode(),
            HttpStatus.OK,
            "SUCCESS",
            null
        );
    }
}

