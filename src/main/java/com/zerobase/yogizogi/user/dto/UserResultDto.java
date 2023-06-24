package com.zerobase.yogizogi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class UserResultDto {

    private String email;
    private String token;
    private String nickName;

}
