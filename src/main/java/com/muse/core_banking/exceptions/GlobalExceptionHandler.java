package com.muse.core_banking.exceptions;

import com.muse.core_banking.handlers.ResponseHandler;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> badRequestHandler(
            BadRequestException exp
    ){

        return ResponseEntity
                .status(BAD_REQUEST.value())
                .body(ResponseHandler.builder()
                        .statusCode(BAD_REQUEST.value())
                        .message(exp.getMessage())
                        .build());
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<?> disabledExceptionHandler(
            DisabledException exp
    ){

        return ResponseEntity
                .status(BAD_REQUEST.value())
                .body(ResponseHandler.builder()
                        .statusCode(BAD_REQUEST.value())
                        .message(exp.getMessage())
                        .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> BadCredential(
            BadCredentialsException exp
    ){

        return ResponseEntity
                .status(BAD_REQUEST.value())
                .body(ResponseHandler.builder()
                        .statusCode(BAD_REQUEST.value())
                        .message(exp.getMessage())
                        .build());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> usernameNotFoundExceptionHandler(
            UsernameNotFoundException exp
    ){

        return ResponseEntity
                .status(NOT_FOUND.value())
                .body(ResponseHandler.builder()
                        .statusCode(NOT_FOUND.value())
                        .message(exp.getMessage())
                        .build());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> entityNotFoundHandler(
            NoHandlerFoundException exp
    ){

        return ResponseEntity
                .status(exp.getStatusCode())
                .body(ResponseHandler.builder()
                        .statusCode(exp.getStatusCode().value())
                        .message(exp.getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> otherExceptionHandler(
            Exception exp
    ){
        return ResponseEntity.status(500)
                .body(ResponseHandler.builder()
                        .statusCode(500)
                        .message(exp.getMessage())
                .build());
    }

}
