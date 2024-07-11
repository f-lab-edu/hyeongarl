package com.hyeongarl.error;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends BaseException {
    private static final String DEFAULT_MESSAGE = "카테고리를 찾을 수 없습니다.";
    private static final String DEFAULT_CODE = "NOTFOUND404";

    public CategoryNotFoundException() {
        super(new ErrorResponse(DEFAULT_MESSAGE, DEFAULT_CODE), HttpStatus.NOT_FOUND);
    }

    public CategoryNotFoundException(String message) {
        super(new ErrorResponse(message, DEFAULT_CODE), HttpStatus.NOT_FOUND);
    }
}
