package com.hyeongarl.error;

import org.springframework.http.HttpStatus;

public class UnAuthorizedException extends BaseException{
    private static final String DEFAULT_MESSAGE = "유효하지 않은 사용자입니다.";
    private static final String DEFAULT_CODE = "CONFLICT409";

    public UnAuthorizedException() {
        super(new ErrorResponse(DEFAULT_MESSAGE, DEFAULT_CODE), HttpStatus.CONFLICT);
    }

    public UnAuthorizedException(String message) {
        super(new ErrorResponse(message, DEFAULT_CODE), HttpStatus.CONFLICT);
    }

}
