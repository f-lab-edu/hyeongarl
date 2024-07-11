package com.hyeongarl.error;

import org.springframework.http.HttpStatus;

public class UrlInvalidException extends BaseException {
    private static final String DEFAULT_MESSAGE = "유효하지 않은 Url입니다.";
    private static final String DEFAULT_CODE = "BADREQUEST400";

    public UrlInvalidException() {
        super(new ErrorResponse(DEFAULT_MESSAGE, DEFAULT_CODE), HttpStatus.BAD_REQUEST);
    }

    public UrlInvalidException(String message) {
        super(new ErrorResponse(message, DEFAULT_CODE), HttpStatus.BAD_REQUEST);
    }
}
