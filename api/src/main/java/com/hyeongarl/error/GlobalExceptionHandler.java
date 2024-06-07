package com.hyeongarl.error;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // @ExceptionHandler : 특정 예외 상황에 대한 처리를 정의할 수 있습니다.
    // 예외를 @ExceptionHandler에서 잡아 적절한 ErrorResponse로 변환하여 일관성 있는 예외를 처리할 수 있습니다.

    // 던질 때 전달하는 ErrorCode를 바탕으로 ErrorResponse를 만들어 발생하고 다른 예외 발생 시 500
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException e) {
        log.error("BaseException", e);
        return new ResponseEntity<>(e.getErrorResponse(), e.getHttpStatus());
    }

    // 지원하지 않는 HTTP 메소드를 호출하는 경우 발생하는 예외입니다. 405
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error("HttpRequestMethodNotSupportedException", e);
        return new ResponseEntity<>(new ErrorResponse("지원하지 않는 형식입니다.", "40005"), HttpStatus.BAD_REQUEST);
    }

    // 지원하지 않는 HTTP 메소드를 호출하는 경우 발생하는 예외입니다. 405
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        String message = "유효하지 않은 값입니다.";
        log.error("MethodArgumentNotValidException ", e);

        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for(ConstraintViolation<?> violation : violations) {
            message = violation.getMessage();
        }

        return new ResponseEntity<>(new ErrorResponse(message, "40401"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)  // 500 반환
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception", e);
        return new ResponseEntity<>(new ErrorResponse(e.getMessage(), "40005"), HttpStatus.BAD_REQUEST);
    }
}
