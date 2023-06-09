package com.zerobase.yogizogi.global.exception;

import com.zerobase.yogizogi.global.Code;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode implements Code {
    //공통
    NOT_ALLOW_ACCESS(HttpStatus.BAD_REQUEST, "허용하지 않는 접근입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, "적절하지 않은 토큰입니다."),
    //USER관련
    NOT_FOUND_USER(HttpStatus.BAD_REQUEST, "존재하지 않는 유저입니다."),
    ALREADY_REGISTER_EMAIL(HttpStatus.BAD_REQUEST, "이미 회원 가입을 완료한 유저입니다."),
    ALREADY_REGISTER_NICK_NAME(HttpStatus.BAD_REQUEST, "이미 등록된 닉네임입니다."),
    ALREADY_REGISTER_PHONE_NUMBER(HttpStatus.BAD_REQUEST, "이미 회원 가입을 완료한 번호입니다."),
    NOT_VALID_PHONE_NUMBER_FORMAT(HttpStatus.BAD_REQUEST,
        "유효한 전화번호 형식이 아닙니다. 01x-0000-0000으로 작성해 주세요."),
    NOT_ACTIVE_USER(HttpStatus.BAD_REQUEST, "아직 계정 활성화가 되지 않은 유저입니다. 인증을 완료해 주십시오."),
    NOT_FOUND_AUTH_KEY(HttpStatus.BAD_REQUEST, "존재하지 않는 인증 코드입니다."),
    NOT_VALID_EMAIL(HttpStatus.BAD_REQUEST, "이메일이 유효하지 않아 메일을 발송하는데 실패했습니다"),
    ALREADY_VERIFY_EMAIL(HttpStatus.BAD_REQUEST, "이미 인증을 완료한 회원입니다."),
    //로그인 관련
    NOT_MATCH_ID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    //로그인(토큰) 관련
    DO_NOT_ALLOW_TOKEN(HttpStatus.BAD_REQUEST, "사용이 허용되지 않는 토큰입니다. 적절한 토큰을 발급 받아 사용해 주세요."),
    ALREADY_EXPIRED_TOKEN(HttpStatus.BAD_REQUEST, "이미 만료된 토큰입니다."),

    //예약 관련
    NOT_FOUND_BOOK(HttpStatus.BAD_REQUEST, "존재하지 않는 예약입니다."),
    ALREADY_BOOKED_ROOM(HttpStatus.BAD_REQUEST, "예약하고자 하는 방이 이미 예약되어 버렸습니다."),
    NOT_ALLOW_DELETE_BOOK(HttpStatus.BAD_REQUEST, "해당 예약은 삭제할 수 업습니다."),
    //리뷰 관련
    NOT_ALLOW_WRITE_REVIEW(HttpStatus.BAD_REQUEST, "리뷰는 사용이 완료된 후 작성이 가능합니다."),
    NOT_FOUND_REVIEW(HttpStatus.BAD_REQUEST, "존재하지 않는 리뷰입니다."),
    NOT_CORRECT_RANGE(HttpStatus.BAD_REQUEST, "평점은 0~10의 정수만 입력이 가능합니다."),
    AlREADY_REGISTER_REVIEW(HttpStatus.BAD_REQUEST, "이미 리뷰를 작성하셨습니다."),

    //숙소 관련

    NOT_FOUND_ACCOMMODATION(HttpStatus.BAD_REQUEST, "존재하지 않는 숙소입니다."),
    //숙소- 방 관련
    NOT_FOUND_ROOM(HttpStatus.BAD_REQUEST, "존재하지 않는 방입니다."),
    NOT_EXISTED_ROOM(HttpStatus.BAD_REQUEST, "사용가능한 방이 없습니다."),


    // 검색 관련
    NOT_CORRECT_DATE(HttpStatus.BAD_REQUEST, "날짜는 2023-07-01 ~ 2023-09-30 까지의 날짜만 검색이 가능합니다."),
    NOT_CORRECT_DATE_RANGE(HttpStatus.BAD_REQUEST, "최대 7일의 정보만 조회가능합니다.");


    private final HttpStatus status;
    private final String msg;
}
