package com.hyeongarl.error;

import org.springframework.http.HttpStatus;

public class UrlAlreadyExistException extends BaseException {
    private static final String DEFAULT_MESSAGE = "이미 존재하는 Url입니다.";
    private static final String DEFAULT_CODE = "CONFLICT409";

    public UrlAlreadyExistException() {
        super(new ErrorResponse(DEFAULT_MESSAGE, DEFAULT_CODE), HttpStatus.CONFLICT);
    }

    public UrlAlreadyExistException(String message) {
        super(new ErrorResponse(message, DEFAULT_CODE), HttpStatus.CONFLICT);
    }
}
