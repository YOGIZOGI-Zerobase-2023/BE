package com.zerobase.yogizogi.user.service;

import com.zerobase.yogizogi.global.ApiResponse;
import com.zerobase.yogizogi.global.ResponseCode;
import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.domain.model.LogInForm;
import com.zerobase.yogizogi.user.repository.UserRepository;
import com.zerobase.yogizogi.user.token.JwtAuthenticationProvider;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserLogInService {

    private final UserRepository userRepository;
    private final JwtAuthenticationProvider provider;
    private final PasswordEncoder passwordEncoder;

    public ApiResponse<?> login(LogInForm logInForm) {

        //존재하지 않는 유저
        AppUser user = userRepository.findByEmail(logInForm.getEmail())
            .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_USER));

        //비번이 일치하지 않는 경우
        if(!validateLogIn(logInForm.getPassword(), user)){
            throw new CustomException(ErrorCode.NOT_MATCH_ID_PASSWORD);
        }
        if (!user.isActive()) {
            throw new CustomException(ErrorCode.NOT_ACTIVE_USER);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("X-AUTH-TOKEN", provider.createToken(user.getEmail(), user.getId()));
        data.put("email", user.getEmail());
        data.put("nickname", user.getNickName());

        return new ApiResponse<>(
            ResponseCode.RESPONSE_SUCCESS.getCode(),
            HttpStatus.OK,
            "SUCCESS",
            data
        );
    }

    private boolean validateLogIn(String rawPassword, AppUser user) {

        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}
