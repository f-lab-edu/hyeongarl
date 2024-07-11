package com.hyeongarl.error;

import org.springframework.http.HttpStatus;

public class CategoryAlreadyExistException extends BaseException {
    private static final String DEFAULT_MESSAGE = "이미 존재하는 카테고리입니다.";
    private static final String DEFAULT_CODE = "CONFLICT409";

    public CategoryAlreadyExistException() {
        super(new ErrorResponse(DEFAULT_MESSAGE, DEFAULT_CODE), HttpStatus.CONFLICT);
    }

    public CategoryAlreadyExistException(String message) {
        super(new ErrorResponse(message, DEFAULT_CODE), HttpStatus.CONFLICT);
    }
}
