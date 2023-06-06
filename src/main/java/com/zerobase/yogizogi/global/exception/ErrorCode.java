package com.zerobase.yogizogi.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    //USER관련
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다."),
    ALREADY_REGISTER_USER(HttpStatus.BAD_REQUEST, "이미 회원 가입을 완료한 유저입니다."),
    ALREADY_REGISTER_EMAIL(HttpStatus.BAD_REQUEST, "이미 회원 가입을 완료한 유저입니다."),
    ALREADY_REGISTER_NICK_NAME(HttpStatus.BAD_REQUEST, "이미 등록된 닉네임입니다."),
    ALREADY_REGISTER_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "이미 회원 가입을 완료한 유저입니다."),
    NOT_VALID_PHONE_NUMBER_FORMAT(HttpStatus.BAD_REQUEST, "유효한 전화번호 형식이 아닙니다. 01x-0000-0000으로 작성해 주세요."),
    NOT_ACTIVE_USER(HttpStatus.BAD_REQUEST, "아직 계정 활성화가 되지 않은 유저입니다. 인증을 완료해 주십시오."),
    NOT_FOUND_AUTH_KEY(HttpStatus.BAD_REQUEST, "존재하지 않는 인증 코드입니다."),

    //로그인 관련
    NOT_MATCH_ID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    endSample(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다.");
    private final HttpStatus httpStatus;
    private final String detail;
}
