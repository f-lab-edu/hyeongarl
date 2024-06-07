package com.hyeongarl.error;

import org.springframework.http.HttpStatus;

public class UserAlreadyExistException extends BaseException {
    private static final String DEFAULT_MESSAGE = "이미 사용중인 이메일입니다.";
    private static final String DEFAULT_CODE = "CONFLICT409";

    public UserAlreadyExistException() {
        super(new ErrorResponse(DEFAULT_MESSAGE, DEFAULT_CODE), HttpStatus.CONFLICT);
    }

    public UserAlreadyExistException(String message) {
        super(new ErrorResponse(message, DEFAULT_CODE), HttpStatus.CONFLICT);
    }
}
