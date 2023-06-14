package com.zerobase.yogizogi.user.controller;

import com.zerobase.yogizogi.user.domain.model.UserSignUpForm;
import com.zerobase.yogizogi.user.service.UserSignUpService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserSignUpController {

   private final UserSignUpService userSignUpService;

   @PostMapping("/sign-up")
   public ResponseEntity<?> signUp(@RequestBody UserSignUpForm userSignUpForm) {
       return ResponseEntity.ok(userSignUpService.signUp(userSignUpForm));
   }
   @GetMapping("/email-verify")
   public ResponseEntity<?> emailAuth(HttpServletRequest request){
       return ResponseEntity.ok(userSignUpService.emailVerify(request.getParameter("id")));
   }
}
