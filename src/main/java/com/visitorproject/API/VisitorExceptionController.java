package com.visitorproject.API;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class VisitorExceptionController {
    @ExceptionHandler(value = RuntimeException.class)
    public RuntimeException exception(RuntimeException exception) {
//        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return exception;
    }

    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<Object> exception(BadCredentialsException exception) {
        return new ResponseEntity<>("Invalid Username or Password, Please enter correct password", HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Object> exception(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }
}
