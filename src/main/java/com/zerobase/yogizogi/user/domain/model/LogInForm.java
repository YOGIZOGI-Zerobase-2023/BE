package com.zerobase.yogizogi.user.domain.model;

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
