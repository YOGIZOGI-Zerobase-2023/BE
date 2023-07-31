package com.zerobase.yogizogi.oauth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.yogizogi.oauth.domain.model.KakaoProfile;
import com.zerobase.yogizogi.oauth.domain.model.OAuthToken;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.repository.UserRepository;
import com.zerobase.yogizogi.user.token.JwtAuthenticationProvider;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class OauthService {

    private final UserRepository userRepository;
    private final JwtAuthenticationProvider provider;
    private final PasswordEncoder encoder;

    public String kakaoProfileCheck(OAuthToken oAuthToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + oAuthToken.getAccess_token());
        httpHeaders.add("Content-type",
            "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(
            httpHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
            "https://kapi.kakao.com/v2/user/me",
            HttpMethod.POST,
            kakaoUserInfoRequest,
            String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile;
        try {
            kakaoProfile = objectMapper.readValue(response.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String mailId = kakaoProfile.getKakao_account().getEmail() + "_" + kakaoProfile.getId();

        String nickName = kakaoProfile.getKakao_account().getProfile().getNickname() + "#" + kakaoProfile.getId();

        if (userRepository.findByEmail(mailId).isPresent()) {
           return oAuthLogin(userRepository.findByEmail(mailId).get());
        } else {
            String randomPassword = UUID.randomUUID().toString();
            return oAuthSignUp(AppUser.builder()
                                        .sns(true)
                                        .email(mailId)
                                        .nickName(nickName)
                                        .password(randomPassword)
                                        .active(true)
                                       .build());
        }
    }
    private String oAuthSignUp(AppUser user){
        String enCodePassword = encoder.encode(user.getPassword());
        user.setPassword(enCodePassword);
        userRepository.save(user);
        return oAuthLogin(user);
    }
    private String oAuthLogin(AppUser user){
        return provider.createToken(user.getEmail(), user.getId(),user.getNickName());
    }
}
