package com.zerobase.yogizogi.oauth.service;

import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.token.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthLoginService {
    private final JwtAuthenticationProvider provider;
    public String oAuthLogin(AppUser user) {
      return provider.createToken(user.getEmail(), user.getId());
    }
}
