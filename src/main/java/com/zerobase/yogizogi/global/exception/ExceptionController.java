package com.zerobase.yogizogi.global.exception;

import com.zerobase.yogizogi.global.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> customExceptionResponseEntity(CustomException e) {
        return ApiResponse.builder().code(e.getErrorCode()).toEntity();
    }
}
