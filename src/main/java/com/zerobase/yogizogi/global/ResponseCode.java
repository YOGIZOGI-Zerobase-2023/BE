package com.zerobase.yogizogi.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ResponseCode implements Code{

    RESPONSE_SUCCESS(HttpStatus.OK, "SUCCESS"),
    RESPONSE_FAIL(HttpStatus.BAD_REQUEST, "FAIL");
    private final HttpStatus status;
    private final String msg;
}