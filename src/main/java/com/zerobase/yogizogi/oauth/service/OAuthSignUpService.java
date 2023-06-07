package com.zerobase.yogizogi.oauth.service;

import com.zerobase.yogizogi.user.common.UserRole;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.dto.LogInForm;
import com.zerobase.yogizogi.user.repository.UserRepository;
import com.zerobase.yogizogi.user.service.UserLogInService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthSignUpService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserLogInService userLogInService;

    public void signUpOAuth(AppUser user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            LogInForm logInForm = LogInForm.builder().email(user.getEmail())
                .password(user.getPassword()).build();
            userLogInService.login(logInForm, UserRole.USER);
        }
        String rawPassword = user.getPassword();
        String enCodePassword = encoder.encode(rawPassword);
        user.setPassword(enCodePassword);
        userRepository.save(user);
    }
}
