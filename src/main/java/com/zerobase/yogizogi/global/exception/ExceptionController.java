package com.zerobase.yogizogi.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @ControllerAdvice 어노테이션을 사용해 예외 처리를 하는 컨트롤러 클래스
 * ExceptionController 클래스는 CustomException 타입의 예외가 발생한 경우에 대한 예외 처리를 담당,
 * 예외 발생 시 '에러 메시지'와 '에러 코드'를 담은 'ResponseEntity를 반환'(400만 반환하지만 메시지가 다양)
 *
 * ExceptionResponse 예외 처리 결과를 클라이언트에 반환하는 클래스
 */
@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ExceptionResponse> customRequestException(final CustomException e) {
        log.warn("api Exception : {}", e.getErrorCode());
        return ResponseEntity.badRequest()
            .body(new ExceptionResponse(e.getMessage(), e.getErrorCode()));
    }

    @Getter
    @AllArgsConstructor
    @ToString
    public static class ExceptionResponse {

        private String message;
        private ErrorCode errorCode;
    }
}
