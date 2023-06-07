package com.zerobase.yogizogi.user.service;

import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import com.zerobase.yogizogi.user.common.UserRole;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.domain.model.LogInForm;
import com.zerobase.yogizogi.user.repository.UserRepository;
import com.zerobase.yogizogi.user.token.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserLogInService {

    private final UserRepository userRepository;
    private final JwtAuthenticationProvider provider;
    private final PasswordEncoder passwordEncoder;

    public String login(LogInForm logInForm, UserRole userRole) {

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
        return provider.createToken(user.getEmail(), user.getId(), userRole);
    }

    private boolean validateLogIn(String rawPassword, AppUser user) {

        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}
