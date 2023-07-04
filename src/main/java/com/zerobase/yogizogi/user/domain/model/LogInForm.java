package com.zerobase.yogizogi.user.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LogInForm {

    private String email;
    private String password;
}
