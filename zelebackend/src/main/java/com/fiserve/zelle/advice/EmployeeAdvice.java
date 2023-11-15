package com.fiserve.zelle.advice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.fiserve.zelle.exception.InvalidDataException;

@RestControllerAdvice
public class EmployeeAdvice {
	
	

		@ExceptionHandler(value=InvalidDataException.class)
		public ResponseEntity<Object> handleCityNotFoundException(InvalidDataException ex, WebRequest request) {

			Map<String, Object> body = new LinkedHashMap<>();
			body.put("timestamp", LocalDateTime.now());
			body.put("message", ex.getMessage());

			return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
		}

		
	}


