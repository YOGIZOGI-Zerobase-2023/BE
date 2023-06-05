package com.zerobase.yogizogi.user.controller;

import com.zerobase.yogizogi.user.dto.UserDto;
import com.zerobase.yogizogi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody UserDto userDto) {
        UserDto createdUser = userService.signUp(userDto);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody UserDto userDto) {
        UserDto loggedInUser = userService.login(userDto);
        return new ResponseEntity<>(loggedInUser, HttpStatus.OK);
    }
}
