package com.hyeongarl.error;

import org.springframework.http.HttpStatus;

public class UserUnInvalidException extends BaseException {
    private static final String DEFAULT_MESSAGE = "인증되지 않은 사용자입니다.";
    private static final String DEFAULT_CODE = "UNAUTHORIZED401";

    public UserUnInvalidException() {
        super(new ErrorResponse(DEFAULT_MESSAGE, DEFAULT_CODE), HttpStatus.UNAUTHORIZED);
    }

    public UserUnInvalidException(String message) {
        super(new ErrorResponse(message, DEFAULT_CODE),  HttpStatus.UNAUTHORIZED);
    }
}
