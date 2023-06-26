package com.zerobase.yogizogi.global;

import org.springframework.http.HttpStatus;



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