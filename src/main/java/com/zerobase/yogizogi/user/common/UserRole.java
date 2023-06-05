package com.zerobase.yogizogi.user.common;

import lombok.Getter;

@Getter
public enum UserRole {
    USER("사용자"),
    HOST("호스트");
    private final String role;

    UserRole(String role){
        this.role = role;
    }
}
