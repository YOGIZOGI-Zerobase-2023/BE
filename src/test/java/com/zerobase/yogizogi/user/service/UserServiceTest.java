package com.zerobase.yogizogi.user.service;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.domain.model.LogInForm;
import com.zerobase.yogizogi.user.domain.model.UserSignUpForm;
import com.zerobase.yogizogi.user.repository.UserRepository;
import com.zerobase.yogizogi.user.smtp.domain.model.MessageForm;
import com.zerobase.yogizogi.user.smtp.service.EmailService;
import com.zerobase.yogizogi.user.token.JwtAuthenticationProvider;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;


class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtAuthenticationProvider provider;
    @Mock
    private PasswordEncoder encoder;
    @Mock
    private EmailService emailService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userRepository, provider, encoder, emailService);
        when(encoder.encode(anyString())).thenAnswer(
            answer -> answer.getArgument(0));//encoder 정상작동을 위한 코드
        when(encoder.matches(anyString(), anyString())).thenAnswer(
            answer -> Objects.equals(answer.getArgument(0), answer.getArgument(1)));
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp_Success() {
        // given
        userService = new UserService(userRepository, provider, encoder, emailService);

        UserSignUpForm userSignUpForm = new UserSignUpForm("test@example.com", "testUser",
            "password");

        when(userRepository.findByEmail(userSignUpForm.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByNickName(userSignUpForm.getNickName())).thenReturn(
            Optional.empty());
        when(encoder.encode(userSignUpForm.getPassword())).thenReturn("password");

        // when
        userService.signUp(userSignUpForm);

        // then
        verify(userRepository, times(1)).findByEmail(userSignUpForm.getEmail());
        verify(userRepository, times(1)).findByNickName(userSignUpForm.getNickName());
        verify(encoder, times(1)).encode(userSignUpForm.getPassword());
        verify(emailService, times(1)).sendMail(any(MessageForm.class));
        ArgumentCaptor<AppUser> userCaptor = ArgumentCaptor.forClass(AppUser.class);
        verify(userRepository, times(1)).save(userCaptor.capture());
        AppUser savedUser = userCaptor.getValue();
        assertEquals("password", savedUser.getPassword());
    }

    @Test
    @DisplayName("이미 등록된 이메일로 회원가입 시도")
    void signUp_AlreadyRegisteredEmail() {
        // given
        userService = new UserService(userRepository, provider, encoder, emailService);

        UserSignUpForm userSignUpForm = new UserSignUpForm("test@example.com", "testUser",
            "password");

        when(userRepository.findByEmail(userSignUpForm.getEmail())).thenReturn(
            Optional.of(mock(AppUser.class)));

        // when
        CustomException exception = assertThrows(CustomException.class,
            () -> userService.signUp(userSignUpForm));

        // then
        assertEquals(ErrorCode.ALREADY_REGISTER_EMAIL, exception.getErrorCode());
        verify(userRepository, times(1)).findByEmail(userSignUpForm.getEmail());
        verify(userRepository, never()).findByNickName(anyString());
        verify(encoder, never()).encode(anyString());
        verify(emailService, never()).sendMail(any(MessageForm.class));
        verify(userRepository, never()).save(any(AppUser.class));
    }

    @Test
    @DisplayName("회원가입 - 이미 등록된 닉네임")
    void signUp_AlreadyRegisteredNickName() {
        // given
        UserSignUpForm userSignUpForm = new UserSignUpForm("test@example.com", "testUser",
            "password");

        when(userRepository.findByEmail(userSignUpForm.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByNickName(userSignUpForm.getNickName())).thenReturn(
            Optional.of(mock(AppUser.class)));
        when(encoder.encode(userSignUpForm.getPassword())).thenReturn("encodedPassword");
        // when
        CustomException exception = assertThrows(CustomException.class,
            () -> userService.signUp(userSignUpForm));

        // then
        assertEquals(ErrorCode.ALREADY_REGISTER_NICK_NAME, exception.getErrorCode());
        verify(userRepository, times(1)).findByEmail(userSignUpForm.getEmail());
        verify(userRepository, times(1)).findByNickName(userSignUpForm.getNickName());
        verify(encoder, never()).encode(userSignUpForm.getPassword());
        verify(emailService, never()).sendMail(any());
        verify(userRepository, never()).save(any(AppUser.class));
    }

    //스프링 시큐리티의 passwordEncoder가 정상적으로 동작하지 않는 문제.
    @Test
    @DisplayName("로그인 성공")
    void login_Success() {
        // given
        userService = new UserService(userRepository, provider, encoder, emailService);

        LogInForm logInForm = new LogInForm("test@example.com", "password");

        AppUser user = new AppUser();
        user.setEmail(logInForm.getEmail());
        String encodedPassword = encoder.encode(logInForm.getPassword());
        user.setPassword(encodedPassword);
        user.setActive(true);
        user.setNickName("testUser");

        when(encoder.encode(anyString())).thenReturn(
            encodedPassword); // encode 메서드는 주어진 입력 값을 그대로 반환하도록 수정
        when(userRepository.findByEmail(logInForm.getEmail())).thenReturn(Optional.of(user));
        when(provider.createToken(user.getEmail(), user.getId(), user.getNickName())).thenReturn(
            "token123");

        // when
        String token = userService.login(logInForm);

        // then
        assertNotNull(token);
        verify(userRepository, times(1)).findByEmail(logInForm.getEmail());
        verify(provider, times(1)).createToken(user.getEmail(), user.getId(), user.getNickName());
        assertTrue(encoder.matches(logInForm.getPassword(), user.getPassword()));

    }

    @Test
    @DisplayName("존재하지 않는 유저로 로그인 시도")
    void login_UserNotFound() {
        // given
        userService = new UserService(userRepository, provider, encoder, emailService);

        LogInForm logInForm = new LogInForm("test@example.com", "password");

        when(userRepository.findByEmail(logInForm.getEmail())).thenReturn(Optional.empty());

        // when
        CustomException exception = assertThrows(CustomException.class,
            () -> userService.login(logInForm));

        // then
        assertEquals(ErrorCode.NOT_FOUND_USER, exception.getErrorCode());
        verify(userRepository, times(1)).findByEmail(logInForm.getEmail());
        verify(provider, never()).createToken(anyString(), anyLong(), anyString());
    }

}
