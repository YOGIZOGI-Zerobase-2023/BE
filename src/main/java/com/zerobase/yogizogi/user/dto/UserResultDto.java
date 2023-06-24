package com.zerobase.yogizogi.user.dto;

import com.zerobase.yogizogi.user.domain.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class UserResultDto {

    private String email;
    private String token;
    private String nickName;

    public static UserResultDto from(AppUser user, String token) {
        return UserResultDto.builder().email(user.getEmail()).token(token).nickName(
            user.getNickName()).build();
    }
}
