package com.project.stayEase.advices;

import com.project.stayEase.customExceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException e) {
        ApiError apiError =ApiError.builder().message(e.getMessage()).build();
       return new ResponseEntity<>(ApiResponse.failureResponse(apiError),HttpStatus.NOT_FOUND);
    }

}
