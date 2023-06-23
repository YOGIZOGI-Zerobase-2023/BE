package com.zerobase.yogizogi.global;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ResponseCode implements Code{

    RESPONSE_SUCCESS {
        @Override
        public HttpStatus getStatus() {
            return HttpStatus.OK;
        }

        @Override
        public String getMsg() {
            return "SUCCESS";
        }
    }
}