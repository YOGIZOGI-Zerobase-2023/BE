package com.zerobase.yogizogi.oauth.service;

import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthSignUpService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    public void signUpOAuth(AppUser user){
        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new CustomException(ErrorCode.ALREADY_REGISTER_USER);
        }
        String rawPassword = user.getPassword();
        String enCodePassword = encoder.encode(rawPassword);
        user.setPassword(enCodePassword);
        userRepository.save(user);
    }
}
