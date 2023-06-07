package com.zerobase.yogizogi.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LogInForm {
    private String email;
    private String password;
}
