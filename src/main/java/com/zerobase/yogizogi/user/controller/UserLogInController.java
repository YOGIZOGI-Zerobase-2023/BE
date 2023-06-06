package com.zerobase.yogizogi.user.controller;

import com.zerobase.yogizogi.user.common.UserRole;
import com.zerobase.yogizogi.user.dto.LogInForm;
import com.zerobase.yogizogi.user.dto.UserDto;
import com.zerobase.yogizogi.user.service.UserLogInService;
import java.net.URI;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserLogInController {
    private final UserLogInService userLogInService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LogInForm logInForm, @RequestParam UserRole userRole) {
        return ResponseEntity.ok(userLogInService.login(logInForm,userRole));
    }

}
