package com.zerobase.yogizogi.user.controller;

import com.zerobase.yogizogi.user.common.UserRole;
import com.zerobase.yogizogi.user.dto.UserSignUpForm;
import com.zerobase.yogizogi.user.service.UserSignUpService;
import java.net.URI;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserSignUpController {

        private final UserSignUpService userSignUpService;

        @PostMapping("/signup")
        public ResponseEntity<?> signUp(@RequestBody UserSignUpForm userSignUpForm, @RequestParam UserRole userRole) {
            return ResponseEntity.ok(userSignUpService.signUp(userSignUpForm,userRole));
        }
        @GetMapping("/email-auth")
        public ResponseEntity<?> emailAuth(HttpServletRequest request){
                return ResponseEntity.ok(userSignUpService.emailAuth(request.getParameter("id")));
        }
}
