package com.sam.scheduler_api.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice // This annotation makes it a "Global Safety Net"
public class GlobalExceptionHandler {

    // Catch "ResourceNotFoundException" specifically
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex) {

        // 1. Create a nice JSON body
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", ex.getMessage());
        body.put("status", 404);

        // 2. Return a 404 Response
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);


    }
}
