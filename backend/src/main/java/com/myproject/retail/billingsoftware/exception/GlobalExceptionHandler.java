package com.myproject.retail.billingsoftware.exception;

import com.myproject.retail.billingsoftware.response.ApiResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@Value("${app.debug:false}")
	private boolean debug;
	
    // Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error("Validation failed: " + fieldErrors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGeneralException(Exception ex) {

    	return ResponseEntity
    	        .status(HttpStatus.INTERNAL_SERVER_ERROR)
    	        .body(ApiResponse.error(
    	                debug ? ex.getMessage() : "Something went wrong"
    	        ));
    }
    
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleDuplicate(DataIntegrityViolationException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error("Resource already exists (duplicate entry)"));
    }
}