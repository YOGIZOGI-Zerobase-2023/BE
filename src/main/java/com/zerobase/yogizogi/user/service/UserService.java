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

        if (userRepository.findByEmail(userSignUpForm.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTER_EMAIL);
        }
//        if(!userSignUpForm.getEmail().contains("@")){
//            throw new CustomException(ErrorCode.NOT_VALID_EMAIL);
//        }
        String rawPassword = userSignUpForm.getPassword();
        String encodePassword = encoder.encode(rawPassword);
        userSignUpForm.setPassword(encodePassword);

        if (userRepository.findByNickName(userSignUpForm.getNickName()).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTER_NICK_NAME);
        }
        userSave(userSignUpForm);
    }

    private void userSave(UserSignUpForm userSignUpForm) {
        String uuid = UUID.randomUUID().toString();
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
                "<div><a target='_blank' href='" + emailUrl + uuid
                    + "'> 로그인을 활성화 하려면 여기를 눌러 주세요. </a></div>"
            ).build());
    }

    public void emailVerify(String uuid) {
        AppUser user = userRepository.findByEmailAuthKey(uuid)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_AUTH_KEY));
        if (user.isActive()) {
            throw new CustomException(ErrorCode.ALREADY_VERIFY_EMAIL);
        }
        user.setActive(true);
        user.setEmailAuthDateTime(LocalDateTime.now());
        userRepository.save(user);
    }


    public String login(LogInForm logInForm) {

        AppUser user = userRepository.findByEmail(logInForm.getEmail())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

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


