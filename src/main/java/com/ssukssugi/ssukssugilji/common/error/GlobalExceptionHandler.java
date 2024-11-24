package com.ssukssugi.ssukssugilji.common.error;

import com.ssukssugi.ssukssugilji.common.error.exception.InvalidRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handleException(Exception e) {
        String message = String.format("Exception thrown: %s", e.getMessage());
        log.error(message);
        return ResponseEntity
            .internalServerError()
            .body(Error.builder()
                .code(ErrorCode.INTERNAL_SERVER_ERROR)
                .message(message)
                .build());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Error> handleRuntimeException(RuntimeException e) {
        String message = String.format("RuntimeException thrown: %s", e.getMessage());
        log.error(message);
        return ResponseEntity
            .internalServerError()
            .body(Error.builder()
                .code(ErrorCode.INTERNAL_SERVER_ERROR)
                .message(message)
                .build());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Error> handleInvalidRequestException(InvalidRequestException e) {
        String message = String.format("InvalidRequestException thrown: %s", e.getMessage());
        log.warn(message);
        return ResponseEntity
            .badRequest()
            .body(Error.builder()
                .code(ErrorCode.INVALID_REQUEST)
                .message(message)
                .build());
    }

}
