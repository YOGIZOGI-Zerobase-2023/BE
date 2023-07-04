package com.zerobase.yogizogi.global;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class ApiResponse<T> {

    private final String code;
    private final HttpStatus status;
    private final String msg;
    private final T data;

    //생성자.
    public ApiResponse(Code code, T data) {
        this.code = code.getCode();
        this.status = code.getStatus();
        this.msg = code.getMsg();
        this.data = data;
    }

    public static <T> ApiResponseBuilder<T> builder() {
        return new ApiResponseBuilder<T>();
    }

    public static class ApiResponseBuilder<T> {

        private Code code;
        private T data;

        ApiResponseBuilder() {
        }

        public ApiResponseBuilder<T> code(Code code) {
            this.code = code;
            return this;
        }

        public ApiResponseBuilder<T> data(T data) {
            this.data = data;
            return this;
        }

        public ResponseEntity<ApiResponse<T>> toEntity() {
            return ResponseEntity.status(this.code.getStatus())
                .body(new ApiResponse<T>(code, data));
        }
    }
}
