package com.zerobase.yogizogi.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다."),
    endSample(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.");
    private final HttpStatus httpStatus;
    private final String detail;
}