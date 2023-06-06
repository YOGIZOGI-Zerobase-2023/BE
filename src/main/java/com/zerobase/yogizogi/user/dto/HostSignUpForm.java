package com.zerobase.yogizogi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostSignUpForm {

    private String email;
    private String password;
    private String phoneNumber;
}