package com.zerobase.yogizogi.user.common;

import lombok.Getter;

@Getter
public enum UserType {
    USER("사용자"),
    HOST("호스트");
    private final String type;

    UserType(String type){
        this.type = type;
    }
}
