package com.hyeongarl.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
    private final ErrorResponse errorResponse;
    private final HttpStatus httpStatus;

    public BaseException(ErrorResponse errorResponse, HttpStatus httpStatus) {
        super(errorResponse.getMessage());
        this.errorResponse = errorResponse;
        this.httpStatus = httpStatus;
    }
}
