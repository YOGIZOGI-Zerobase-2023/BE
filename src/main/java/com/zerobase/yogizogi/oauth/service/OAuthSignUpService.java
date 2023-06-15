package com.zerobase.yogizogi.oauth.service;

import com.zerobase.yogizogi.global.ApiResponse;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthSignUpService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final OAuthLoginService oAuthLoginService;
    public ApiResponse<?> signUpOAuth(AppUser user) {
        String rawPassword = user.getPassword();
        String enCodePassword = encoder.encode(rawPassword);
        user.setPassword(enCodePassword);
        userRepository.save(user);
        return oAuthLoginService.oAuthLogin(user);
    }
}
