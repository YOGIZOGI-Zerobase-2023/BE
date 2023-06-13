package com.zerobase.yogizogi.user.controller;

import com.zerobase.yogizogi.user.domain.model.LogInForm;
import com.zerobase.yogizogi.user.service.UserLogInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserLogInController {

    private final UserLogInService userLogInService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LogInForm logInForm) {
        return ResponseEntity.ok(userLogInService.login(logInForm));
    }

}
