package com.zerobase.yogizogi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Builder
@Setter
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String email;
}
