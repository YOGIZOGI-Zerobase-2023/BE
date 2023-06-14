package com.zerobase.yogizogi.global;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * The {@code ApiResponse} 클래스는 코드, 상태, 메시지 및 데이터를 포함하는 제네릭 API 응답을 나타냅니다.
 *
 * @param <T> 는 반환할 데이터를 담는 매개변수입니다.
 */
@Data
@AllArgsConstructor
@Getter
public class ApiResponse<T> {
    private String code;
    private HttpStatus status;
    private String msg;
    private T data;


    public ApiResponse(Code code,T data){
        this.code=code.getCode();
        this.status=code.getStatus();
        this.msg=code.getMsg();
        this.data=data;
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

        public ApiResponse<T> build() {
            return new ApiResponse<T>(code, data);
        }
        public ResponseEntity<?> toEntity(){
            return ResponseEntity.status(this.code.getStatus()).body(new ApiResponse<T>(code,data));
        }

        public String toString() {
            return "ApiResponse.ApiResponseBuilder(code=" + this.code + ", data=" + this.data + ")";
        }
    }
}
