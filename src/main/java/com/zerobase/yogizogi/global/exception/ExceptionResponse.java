package com.zerobase.yogizogi.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class ExceptionResponse {
    private String message;
    private ErrorCode errorCode;
}
