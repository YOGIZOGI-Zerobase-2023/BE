package com.zerobase.yogizogi.user.controller;

import com.zerobase.yogizogi.global.ApiResponse;
import com.zerobase.yogizogi.global.ResponseCode;
import com.zerobase.yogizogi.user.domain.model.LogInForm;
import com.zerobase.yogizogi.user.domain.model.UserSignUpForm;
import com.zerobase.yogizogi.user.repository.UserRepository;
import com.zerobase.yogizogi.user.service.UserService;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Value("${mainpage.url}")
    private String MAINPAGE;

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<Object>> signUp(@RequestBody UserSignUpForm userSignUpForm) {
        userService.signUp(userSignUpForm);
        Map<String, String> msg = new HashMap<>();
        msg.put("msg", "회원 가입에 성공했습니다.");
        return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(msg).toEntity();
    }

    @GetMapping("/email-verify")
    public void emailAuth(HttpServletRequest request, HttpServletResponse response)
        throws IOException {
        userService.emailVerify(request.getParameter("id"));
        response.sendRedirect(MAINPAGE);
    }
        @PostMapping("/login")
        public ResponseEntity<ApiResponse<Object>> login (@RequestBody LogInForm logInForm){
            String token = userService.login(logInForm);
            Map<String, String> data = new TreeMap<>();

            data.put("X-AUTH-TOKEN", token);
            data.put("email", logInForm.getEmail());
            data.put("nickname", userRepository.findByEmail(logInForm.getEmail()).get().getNickName());
            return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(data).toEntity();
        }
    }
