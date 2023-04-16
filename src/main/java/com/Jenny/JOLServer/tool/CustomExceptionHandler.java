package com.Jenny.JOLServer.tool;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex) throws Exception {
        return new ResponseEntity<>(ErrorResponse.builder().code(HttpStatus.BAD_REQUEST.value()).msg(ex.getMessage()).build(), HttpStatus.BAD_REQUEST);
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorResponse {
        private int code;
        private String msg;
    }
}

