package com.zerobase.yogizogi.user.controller;

import com.zerobase.yogizogi.user.domain.model.LogInForm;
import com.zerobase.yogizogi.user.domain.model.UserSignUpForm;
import com.zerobase.yogizogi.user.service.UserService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody UserSignUpForm userSignUpForm) {
        return ResponseEntity.ok(userService.signUp(userSignUpForm));
    }

    @GetMapping("/email-verify")
    public ResponseEntity<?> emailAuth(HttpServletRequest request) {
        return ResponseEntity.ok(userService.emailVerify(request.getParameter("id")));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LogInForm logInForm) {
        return ResponseEntity.ok(userService.login(logInForm));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return ResponseEntity.ok(userService.logout(request));
    }
}
