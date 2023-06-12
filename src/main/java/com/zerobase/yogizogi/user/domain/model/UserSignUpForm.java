package com.zerobase.yogizogi.user.domain.model;

import com.zerobase.yogizogi.user.common.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSignUpForm {

    private String email;
    private String nickName;
    private String bookName;
    private String password;
    private String phoneNumber;
    private UserRole userRole;
}