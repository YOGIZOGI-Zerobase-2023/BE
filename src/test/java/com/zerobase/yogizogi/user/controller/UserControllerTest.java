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
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService, userRepository);
    }

    @Test
    @DisplayName("회원가입_성공")
    void signUp_Success() {
        // given
        UserSignUpForm userSignUpForm = new UserSignUpForm("test@example.com", "testUser",
            "password");

        when(userRepository.findByEmail(userSignUpForm.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByNickName(userSignUpForm.getNickName())).thenReturn(
            Optional.empty());

        // when
        ResponseEntity<ApiResponse<Object>> response = userController.signUp(userSignUpForm);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ResponseCode.RESPONSE_SUCCESS.toString(), response.getBody().getCode());
        verify(userService, times(1)).signUp(userSignUpForm);
    }

    /**
     * 아래 테스트 사용방법 Controller의 변수
     *
     * @Value("${mainpage.url}") private String MAINPAGE; 아래와 같이 수정해야 합니다. public void
     * emailAuth(HttpServletRequest request, HttpServletResponse response, @Value("${mainpage.url}")
     * String MAINPAGE) 위 처럼 만들고 필드 변수 삭제
     * <p>
     * 이렇게 하지 않으면 필드변수
     * <p>
     * userController.emailAuth(request, response, mainPage); 해당 형태의 메서드를 사용할 때, MAINPAGE는 해당 클래스를
     * 선언하며 null이 되기 떄문
     */
//    @Test
//    @DisplayName("이메일 인증 - 리다이렉트 성공")
//    void testEmailAuth_SuccessfulRedirect() throws Exception {
//        // given
//        String userUUID = "test-uuid";
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        MockHttpServletResponse response = new MockHttpServletResponse();
//        String mainPage = "http://www.example.com";
//        request.setParameter("id", userUUID);
//
//        // when
//        userController.emailAuth(request, response,mainPage);
//
//        // then
//        verify(userService).emailVerify(userUUID);
//        assertEquals(mainPage, response.getRedirectedUrl());
//    }
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
