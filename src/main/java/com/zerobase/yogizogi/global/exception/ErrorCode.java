package com.zerobase.yogizogi.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    //USER관련
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다."),
    ALREADY_REGISTER_EMAIL(HttpStatus.BAD_REQUEST, "이미 회원 가입을 완료한 유저입니다."),
    ALREADY_REGISTER_NICK_NAME(HttpStatus.BAD_REQUEST, "이미 등록된 닉네임입니다."),
    ALREADY_REGISTER_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "이미 회원 가입을 완료한 번호입니다."),
    NOT_VALID_PHONE_NUMBER_FORMAT(HttpStatus.BAD_REQUEST, "유효한 전화번호 형식이 아닙니다. 01x-0000-0000으로 작성해 주세요."),
    NOT_ACTIVE_USER(HttpStatus.BAD_REQUEST, "아직 계정 활성화가 되지 않은 유저입니다. 인증을 완료해 주십시오."),
    NOT_FOUND_AUTH_KEY(HttpStatus.BAD_REQUEST, "존재하지 않는 인증 코드입니다."),
    NOT_VALID_EMAIL(HttpStatus.BAD_REQUEST,"이메일이 유효하지 않아 메일을 발송하는데 실패했습니다"),

    //로그인 관련
    NOT_MATCH_ID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    //로그인(토큰) 관련
    DO_NOT_ALLOW_TOKEN(HttpStatus.BAD_REQUEST, "사용이 허용되지 않는 토큰입니다. 적절한 토큰을 발급 받아 사용해 주세요."),

    //예약 관련
    NOT_FOUND_BOOK(HttpStatus.BAD_REQUEST, "존재하지 않는 예약입니다."),
    ALREADY_BOOKED_ROOM(HttpStatus.BAD_REQUEST, "예약하고자 하는 방이 이미 예약되어 버렸습니다."),

    //공통
    NOT_ALLOW_DELETE(HttpStatus.BAD_REQUEST, "해당 유저는 삭제할 권한이 없습니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "적절하지 않은 토큰입니다."),

    //리뷰 관련
    NOT_FOUND_REVIEW(HttpStatus.BAD_REQUEST, "존재하지 않는 리뷰입니다."),
    NOT_CORRECT_RANGE(HttpStatus.BAD_REQUEST, "평점은 0~10의 정수만 입력이 가능합니다."),
    AlREADY_REGISTER_REVIEW(HttpStatus.BAD_REQUEST, "이미 리뷰를 작성하셨습니다."),

    //숙소 관련
    USER_NOT_ALLOW_ACCESS(HttpStatus.BAD_REQUEST, "유저는 방 등록을 할 수 없습니다."),
    NOT_FOUND_ACCOMMODATION(HttpStatus.BAD_REQUEST, "존재하지 않는 숙소입니다."),
    //숙소- 방 관련
    NOT_FOUND_ROOM(HttpStatus.BAD_REQUEST, "존재하지 않는 숙소입니다."),
    endSample(HttpStatus.BAD_REQUEST, "마무리 샘플입니다.");



    private final HttpStatus httpStatus;
    private final String detail;
}
