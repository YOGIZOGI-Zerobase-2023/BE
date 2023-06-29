package com.zerobase.yogizogi.user.controller;

import com.zerobase.yogizogi.global.ApiResponse;
import com.zerobase.yogizogi.global.ResponseCode;
import com.zerobase.yogizogi.user.domain.model.UserSignUpForm;
import com.zerobase.yogizogi.user.repository.UserRepository;
import com.zerobase.yogizogi.user.service.UserService;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("회원가입_성공")
    void signUp_Success() {
        // given
        UserController userController = new UserController(userService, userRepository);
        UserSignUpForm userSignUpForm = new UserSignUpForm("test@example.com", "testUser", "password");

        // Mock userRepository.findByEmail to return an empty Optional, indicating that the email is not registered yet.
        when(userRepository.findByEmail(userSignUpForm.getEmail())).thenReturn(Optional.empty());

        // Mock userRepository.findByNickName to return an empty Optional, indicating that the nickname is not registered yet.
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
}
