package com.zerobase.yogizogi.user.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor//for test //test 통과하기 때문에 필요하지 않음.
public class LogInForm {
    private String email;
    private String password;
}
