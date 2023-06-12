package com.zerobase.yogizogi.user.service;

import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import com.zerobase.yogizogi.user.common.UserRole;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.domain.model.HostSignUpForm;
import com.zerobase.yogizogi.user.domain.model.UserSignUpForm;
import com.zerobase.yogizogi.user.repository.UserRepository;
import com.zerobase.yogizogi.user.smtp.domain.model.MessageForm;
import com.zerobase.yogizogi.user.smtp.service.EmailService;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserSignUpService {

    private final UserRepository userRepository;
    private final EmailService emailService;

    private final PasswordEncoder encoder;

    public String signUp(UserSignUpForm userSignUpForm, UserRole userRole) {
        // userSignUpForm, PhoneNumber 양식 맞는지, 겹치는지 확인(All)
        // nickName 겹치는지 확인(USER_Only)

        //이미 회원가입 한 경우.
        if (userRepository.findByEmail(userSignUpForm.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTER_EMAIL);
        }
        //(230608)이메일 형식은 프론트에서 email로 값을 주면 이외의 가입이 불가능하므로 예외처리를 따로 두지 않음.

        //전화번호가 이미 등록 되었는지
        if (userRepository.findByPhoneNumber(userSignUpForm.getPhoneNumber()).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_REGISTER_PHONE_NUMBER);
        }
        //비밀번호 encoding
        String rawPassword = userSignUpForm.getPassword();
        String encodePassword = encoder.encode(rawPassword);
        userSignUpForm.setPassword(encodePassword);

        //전화번호 양식이 정상적인지
        if (!userSignUpForm.getPhoneNumber().matches("^(01[016-9])-(\\d{3,4})-(\\d{4})$")) {
            throw new CustomException(ErrorCode.NOT_VALID_PHONE_NUMBER_FORMAT);
        }
        if (userRole.equals(UserRole.USER)) {
            //닉네임이 이미 등록되어 있는 것은 아닌지
            if (userRepository.findByNickName(userSignUpForm.getNickName()).isPresent()) {
                throw new CustomException(ErrorCode.ALREADY_REGISTER_NICK_NAME);
            }
            userSave(userSignUpForm);
        } else {
            HostSignUpForm hostSignUpForm = HostSignUpForm.builder()
                .email(userSignUpForm.getEmail()).password(userSignUpForm.getPassword())
                .phoneNumber(userSignUpForm.getPhoneNumber()).build();
            hostSave(hostSignUpForm);
        }
        return "/signup/success";
    }


    public String emailAuth(String uuid) {
        AppUser user = userRepository.findByEmailAuthKey(uuid)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_AUTH_KEY));
        user.setActive(true);
        user.setEmailAuthDateTime(LocalDateTime.now());
        userRepository.save(user);

        return "users/active";
    }

    private void userSave(UserSignUpForm userSignUpForm) {
        String uuid = UUID.randomUUID().toString();
        userRepository.save(AppUser.builder().sns(false).email(userSignUpForm.getEmail())
            .nickName(userSignUpForm.getNickName()).bookName(userSignUpForm.getBookName())
            .password(userSignUpForm.getPassword()).userRole(UserRole.USER).phoneNumber(
                userSignUpForm.getPhoneNumber()).emailAuthKey(uuid).active(false).build());
        mailSend(uuid, userSignUpForm.getEmail());
    }

    private void hostSave(HostSignUpForm hostSignUpForm) {
        String uuid = UUID.randomUUID().toString();
        userRepository.save(AppUser.builder().sns(false).email(hostSignUpForm.getEmail())
            .password(hostSignUpForm.getPassword()).userRole(UserRole.HOST).phoneNumber(
                hostSignUpForm.getPhoneNumber()).emailAuthKey(uuid).active(false).build());
        mailSend(uuid, hostSignUpForm.getEmail());
    }

    private void mailSend(String uuid, String to) {
        emailService.sendMail(MessageForm.builder().to(to).subject("회원 활성화 인증 메일").message(
            "<div><a target='_blank' href='http://localhost:8080/users/email-auth?id=" + uuid
                + "'> 로그인을 활성화 하려면 여기를 눌러 주세요. </a></div>").build());
    }
}
