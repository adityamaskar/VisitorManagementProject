package com.visitorproject.API;

import com.visitorproject.service.UserProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Arrays;

@ControllerAdvice
public class VisitorExceptionController {

    private static final Logger logger = LoggerFactory.getLogger(VisitorExceptionController.class);
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException exception) {
        logger.error(exception.getMessage() + " : "+ Arrays.toString(exception.getStackTrace()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse(LocalDateTime.now().toString(), 500,HttpStatus.INTERNAL_SERVER_ERROR.toString(),exception.getMessage()));
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException exception) {
        logger.error(exception.getMessage() + " : "+ Arrays.toString(exception.getStackTrace()));
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(createErrorResponse(LocalDateTime.now().toString(), 500,HttpStatus.FORBIDDEN.toString(),"Username or Password is wrong"));
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public ResponseEntity<Object> handleAuthException(AuthenticationException exception) {
        logger.error(exception.getMessage() + " : "+ Arrays.toString(exception.getStackTrace()));
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(createErrorResponse(LocalDateTime.now().toString(), 403,HttpStatus.FORBIDDEN.toString(),exception.getMessage()));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception exception) {
        logger.error(exception.getMessage() + " : "+ Arrays.toString(exception.getStackTrace()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResponse(LocalDateTime.now().toString(), 500,HttpStatus.INTERNAL_SERVER_ERROR .toString(),exception.getMessage()));
    }

    private ErrorResponse createErrorResponse(String timestamp, Integer status, String error, String message) {
        return ErrorResponse.builder().timestamp(timestamp).status(status).error(error).message(message).build();
    }
}
