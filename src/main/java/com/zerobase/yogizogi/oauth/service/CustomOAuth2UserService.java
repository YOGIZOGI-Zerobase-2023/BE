//package com.zerobase.yogizogi.oauth.service;
//
//import com.zerobase.yogizogi.oauth.domain.dto.OAuthToken;
//import com.zerobase.yogizogi.user.common.UserRole;
//import com.zerobase.yogizogi.user.domain.entity.AppUser;
//import com.zerobase.yogizogi.user.repository.UserRepository;
//import java.util.Collections;
//import javax.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
//
//    private final UserRepository userRepository;
//    private final HttpSession httpSession;
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest)
//        throws OAuth2AuthenticationException {
//        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
//        OAuth2User oAuth2User = oAuth2UserService.loadUser(oAuth2UserRequest);
//
//        // 진행중인 서비스 구분 위해 문자열로 받음.
//        // oAuth2UserRequest.getClientRegistration().getRegistrationId() 값이 들어있다.
//        // {registrationId='kakao'} 이런 형태
//        String registrationId = oAuth2UserRequest.getClientRegistration().getRegistrationId();
//
//        // OAuth2 로그인 시 키 값이 된다.
//        // 구글은 키 값이 "sub"이고,
//        // 네이버는 "response"이고,
//        // 카카오는 "id" 각각 다르므로 이렇게 따로 변수로 받아서 넣어줘야함.(카카오만 하지만 이와 같이 통합 구현)
//        String userNameAttributeName = oAuth2UserRequest.getClientRegistration()
//            .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
//
//        // OAuth2 로그인을 통해 가져온 OAuth2User의 attribute를 담아주는 of 메소드.
//        OAuthToken attributes = OAuthToken.of(registrationId, userNameAttributeName,
//            oAuth2User.getAttributes());
//
//        AppUser user = getUser(attributes);
//        httpSession.setAttribute("user", new SessionUser(user));
//
//        System.out.println(attributes.getAttributes());
//        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(UserRole.USER))
//            , attributes.getAttributes()
//            , attributes.getNameAttributeKey());
//    }
//
//    private AppUser getUser(OAuthToken attributes) {
//        AppUser user = AppUser.builder().build();
//        userRepository.save(user);
//        return user;
//    }
//
//    //OAuth 경우 업데이트 자체를 불가능.
//
//}
