package com.javadev.jobportal.exceptions;

import com.javadev.jobportal.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomWebServiceException.class)
    public ResponseEntity<?> handleCustomWebServiceException(CustomWebServiceException e) {
        HttpStatus status = e.getHttpStatus() == null ? HttpStatus.INTERNAL_SERVER_ERROR : e.getHttpStatus();
        return new ResponseEntity<>(
                new ApiResponse<>(status,e.getMessage()),
                 status
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String,String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            errors.put(((FieldError)error).getField(),error.getDefaultMessage());
        });

        return new ResponseEntity<>(
                new ApiResponse<>(HttpStatus.BAD_REQUEST,null, errors),
                HttpStatus.BAD_REQUEST
        );
    }
}
