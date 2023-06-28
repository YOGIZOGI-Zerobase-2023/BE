package com.zerobase.yogizogi.user.controller;

import com.zerobase.yogizogi.global.ApiResponse;
import com.zerobase.yogizogi.global.ResponseCode;
import com.zerobase.yogizogi.user.domain.model.LogInForm;
import com.zerobase.yogizogi.user.domain.model.UserSignUpForm;
import com.zerobase.yogizogi.user.repository.UserRepository;
import com.zerobase.yogizogi.user.service.UserService;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Value("${frontend.url}")
    private String frontendUrl;
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
    public ResponseEntity<ApiResponse<Object>> emailAuth(HttpServletRequest request) {
        userService.emailVerify(request.getParameter("id"));
        Map<String, String> msg = new HashMap<>();
        msg.put("msg", "회원 가입 인증이 완료 되었습니다.");
        msg.put("메인페이지로 이동하기", MAINPAGE);//메인페이지로 가는 등의 형태가 필요할 것으로 보임.
        //메인페이지로 리다이렉트 하는 방법(?)
        return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(msg).toEntity();
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<Object>> login(@RequestBody LogInForm logInForm) {
        String token = userService.login(logInForm);
        Map<String, String> data = new TreeMap<>();

        data.put("X-AUTH-TOKEN", token);
        data.put("email", logInForm.getEmail());
        //서비스에서 검증했기 때문에 사용 가능.
        data.put("nickname", userRepository.findByEmail(logInForm.getEmail()).get().getNickName());
        return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(data).toEntity();
    }

    @DeleteMapping("/logout")
    public ResponseEntity<ApiResponse<Object>> logout(HttpServletRequest request) {
        userService.logout(request);
        return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).toEntity();
    }
}
