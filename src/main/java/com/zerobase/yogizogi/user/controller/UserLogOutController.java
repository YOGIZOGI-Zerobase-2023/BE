package com.zerobase.yogizogi.user.controller;

import com.zerobase.yogizogi.user.service.UserLogOutService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/user")
public class UserLogOutController {
    private final UserLogOutService userLogOutService;
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request){
        return ResponseEntity.ok(userLogOutService.logout(request));
    }
}
