package com.zerobase.yogizogi.global.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode){
        super(errorCode.getMsg());
        this.errorCode=errorCode;
    }
}
