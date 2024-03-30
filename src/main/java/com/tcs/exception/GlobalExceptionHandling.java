package com.tcs.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandling {
	
	
	@ExceptionHandler(VideoNotFoundException.class)
	public ResponseEntity<?> handleVideoNotFoundException(VideoNotFoundException ex) {
		return ResponseEntity.status(HttpStatus.CREATED).body(ex.getMessage());
	}

}
