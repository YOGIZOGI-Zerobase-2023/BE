package com.zerobase.yogizogi.user.service;

import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.domain.model.LogInForm;
import com.zerobase.yogizogi.user.domain.model.UserSignUpForm;
import com.zerobase.yogizogi.user.repository.UserRepository;
import com.zerobase.yogizogi.user.smtp.domain.model.MessageForm;
import com.zerobase.yogizogi.user.smtp.service.EmailService;
import com.zerobase.yogizogi.user.token.JwtAuthenticationProvider;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtAuthenticationProvider provider;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final PasswordEncoder encoder;
    @Value("${email.redirect.url}")
    private String emailUrl;

    public void signUp(UserSignUpForm userSignUpForm) {

        //이미 회원가입 한 경우.
        if (userRepository.findByEmail(userSignUpForm.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTER_EMAIL);
        }

        //비밀번호 encoding
        String rawPassword = userSignUpForm.getPassword();
        String encodePassword = encoder.encode(rawPassword);
        userSignUpForm.setPassword(encodePassword);

        //닉네임이 이미 등록되어 있는 것은 아닌지
        if (userRepository.findByNickName(userSignUpForm.getNickName()).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTER_NICK_NAME);
        }
        userSave(userSignUpForm);
    }


    private void userSave(UserSignUpForm userSignUpForm) {
        String uuid = UUID.randomUUID().toString();
        //해당 서비스의 위치로 성패를 변경 가능. 테스트에는 해당 메서드를 아래에 두어 모두 수용함.
        mailSend(uuid, userSignUpForm.getEmail());

        userRepository.save(
            AppUser
                .builder()
                .sns(false)
                .email(userSignUpForm.getEmail())
                .nickName(userSignUpForm.getNickName())
                .password(userSignUpForm.getPassword())
                .emailAuthKey(uuid)
                .active(false)
                .build()
        );

    }

    private void mailSend(String uuid, String to) {

        emailService.sendMail(MessageForm.builder().to(to).subject("회원 활성화 인증 메일")
            .message(
                "<div><a target='_blank' href='"+emailUrl + uuid
                    + "'> 로그인을 활성화 하려면 여기를 눌러 주세요. </a></div>"
            ).build());
    }

    public void emailVerify(String uuid) {
        AppUser user = userRepository.findByEmailAuthKey(uuid)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_AUTH_KEY));
        if(user.isActive()){
            throw new CustomException(ErrorCode.ALREADY_VERIFY_EMAIL);
        }
        user.setActive(true);
        user.setEmailAuthDateTime(LocalDateTime.now());
        userRepository.save(user);
        //TODO_이메일 발송 실패에 관한 로직 처리가 되어 있지 않습니다.
    }


    public String login(LogInForm logInForm) {

        //존재하지 않는 유저
        AppUser user = userRepository.findByEmail(logInForm.getEmail())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        //비번이 일치하지 않는 경우
        if (!validateLogIn(logInForm.getPassword(), user)) {
            throw new CustomException(ErrorCode.NOT_MATCH_ID_PASSWORD);
        }
        if (!user.isActive()) {
            throw new CustomException(ErrorCode.NOT_ACTIVE_USER);
        }
        return provider.createToken(user.getEmail(), user.getId(),
            user.getNickName());
    }

    private boolean validateLogIn(String rawPassword, AppUser user) {
        return passwordEncoder.matches(rawPassword, user.getPassword());
    }
}


