package com.zerobase.yogizogi.user.service;

import com.zerobase.yogizogi.user.dto.UserDto;
import com.zerobase.yogizogi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository UserRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDto signUp(UserDto userDto) {
        // 회원 가입 로직 구현
        // userDto에서 필요한 정보를 추출하여 AppUser 엔티티를 생성하고 저장합니다.
        // 암호화된 비밀번호를 저장하기 위해 setPassword 메서드를 사용하세요.
        // 회원 가입 완료 후 생성된 사용자 정보를 UserDto로 변환하여 반환합니다.
    }

    public UserDto login(UserDto userDto) {
        // 로그인 로직 구현
        // userDto에서 필요한 정보를 추출하여 AppUser를 조회합니다.
        // 조회된 사용자와 입력된 비밀번호를 비교하여 인증 처리를 합니다.
        // 인증된 사용자 정보를 UserDto로 변환하여 반환합니다.
    }
}
