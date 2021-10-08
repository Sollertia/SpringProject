package com.sollertia.noticeboard.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public enum ErrorCode {
    // 400 Bad Request
    CONTAIN_PASSWORD(HttpStatus.BAD_REQUEST, "400_1", "비밀번호에 닉네임이 포함되어서는 안됩니다."),
    CHEK_PASSWORD(HttpStatus.BAD_REQUEST, "400_2", "비밀번호를 다시 확인해주세요")
    ;

    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String errorMessage;

    ErrorCode(HttpStatus httpStatus, String errorCode, String errorMessage) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}
