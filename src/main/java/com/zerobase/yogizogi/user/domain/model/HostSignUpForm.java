package com.zerobase.yogizogi.user.domain.model;

import com.zerobase.yogizogi.user.common.UserRole;
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
    private UserRole userRole;
}