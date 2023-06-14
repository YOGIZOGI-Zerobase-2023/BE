package com.zerobase.yogizogi.global;

import org.springframework.http.HttpStatus;

public interface Code {

    String code = null;
    HttpStatus status = null;
    String msg = null;

    default String getCode() {
        return this.toString();
    }

    HttpStatus getStatus();

    String getMsg();

}
