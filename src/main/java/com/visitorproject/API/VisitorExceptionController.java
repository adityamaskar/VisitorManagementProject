package com.visitorproject.API;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class VisitorExceptionController {
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse(LocalDateTime.now().toString(), 500,HttpStatus.INTERNAL_SERVER_ERROR.toString(),exception.getMessage()));
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(createErrorResponse(LocalDateTime.now().toString(), 500,HttpStatus.INTERNAL_SERVER_ERROR.toString(),exception.getMessage()));
    }

//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<Object> handleGenericException(Exception exception) {
//        return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                .body(createErrorResponse("Forbidden", exception.getMessage()));
//    }
//
    private ErrorResponse createErrorResponse(String timestamp, Integer status, String error, String message) {
        return ErrorResponse.builder().timestamp(timestamp).status(status).error(error).message(message).build();
    }
}
