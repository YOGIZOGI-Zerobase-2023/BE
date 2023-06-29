package com.zerobase.yogizogi.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerobase.yogizogi.global.ApiResponse;
import com.zerobase.yogizogi.global.ResponseCode;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.domain.model.LogInForm;
import com.zerobase.yogizogi.user.domain.model.UserSignUpForm;
import com.zerobase.yogizogi.user.repository.UserRepository;
import com.zerobase.yogizogi.user.service.UserService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    private UserController userController;
    private String mainPageUrl;
    @BeforeEach
    void setUp() {
        userController = new UserController(userService, userRepository);
    }
    @Test
    @DisplayName("회원가입_성공")
    void signUp_Success() {
        // given
        UserController userController = new UserController(userService, userRepository);
        UserSignUpForm userSignUpForm = new UserSignUpForm("test@example.com", "testUser", "password");

        when(userRepository.findByEmail(userSignUpForm.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByNickName(userSignUpForm.getNickName())).thenReturn(Optional.empty());

        // when
        ResponseEntity<ApiResponse<Object>> response = userController.signUp(userSignUpForm);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseCode.RESPONSE_SUCCESS.toString(), response.getBody().getCode());

        Map<String, String> expectedData = new HashMap<>();
        expectedData.put("msg", "회원 가입에 성공했습니다.");
        assertEquals(expectedData, response.getBody().getData());

        verify(userService, times(1)).signUp(userSignUpForm);
    }

    @Test
    @DisplayName("로그인 성공")
    void login_Success() {
        // given
        LogInForm logInForm = new LogInForm("test@example.com", "password");

        String token = "token123";
        String email = logInForm.getEmail();
        String nickname = "testUser";

        AppUser mockUser = new AppUser();
        mockUser.setNickName(nickname);

        when(userService.login(logInForm)).thenReturn(token);
        when(userRepository.findByEmail(logInForm.getEmail())).thenReturn(Optional.of(mockUser));

        // when
        ResponseEntity<ApiResponse<Object>> response = userController.login(logInForm);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseCode.RESPONSE_SUCCESS.toString(), response.getBody().getCode());

        Map<String, String> expectedData = new HashMap<>();
        expectedData.put("X-AUTH-TOKEN", token);
        expectedData.put("email", email);
        expectedData.put("nickname", nickname);
        assertEquals(expectedData, response.getBody().getData());

        verify(userService, times(1)).login(logInForm);
        verify(userRepository, times(1)).findByEmail(logInForm.getEmail());
    }
}
