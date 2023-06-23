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

@AllArgsConstructor
@Getter
public class ApiResponse<T> {

    private String code;
    private HttpStatus status;
    private String msg;
    private T data;

//생성자.
    public ApiResponse(Code code, T data) {
        this.code = code.getCode();
        this.status = code.getStatus();
        this.msg = code.getMsg();
        this.data = data;
    }
    //정적 팩토리 메서드
    //ApiResponse(Code code, T data): Code와 데이터를 인자로 받아 응답 객체를 생성합니다.
    // code에서 응답 코드, 상태, 메시지를 가져와 필드에 설정합니다.
    public static <T> ApiResponseBuilder<T> builder() {
        return new ApiResponseBuilder<T>();
    }


    //정적 내부 클래스
    //builder(): ApiResponseBuilder 객체를 반환하여 응답 객체를 생성하는 빌더 패턴을 사용할 수 있도록 합니다.
    public static class ApiResponseBuilder<T> {

        private Code code;
        private T data;
        //생성자.
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

        public ResponseEntity<ApiResponse<T>>toEntity() {
            return ResponseEntity.status(this.code.getStatus())
                .body(new ApiResponse<T>(code, data));
        }
    }
}
