package com.vaccine.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.vaccine.payloads.response.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleUnknownException(Exception e) {
		log.error("Exception: {}", e.getMessage());
		log.error("Sub Exception: {}", e.getClass().getCanonicalName());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown error", null));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error("MethodArgumentNotValidException: {}", e.getMessage());

		Map<String, String> errors = new HashMap<String, String>();
		e.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(
				HttpStatus.BAD_REQUEST,
				"Validation failed",
				errors));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		log.error("HttpMessageNotReadableException: {}", e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(
				HttpStatus.BAD_REQUEST,
				"Required request body is missing",
				null));
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
		log.error("HttpRequestMethodNotSupportedException: {}", e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(
				HttpStatus.BAD_REQUEST,
				"Method not allowed",
				null));
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException e) {
		log.error("AccessDeniedException: {}", e.getMessage());

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(
				HttpStatus.UNAUTHORIZED,
				e.getMessage(),
				null));
	}

	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<?> handleDisabledException(DisabledException e) {
		log.error("DisabledException: {}", e.getMessage());

		return ResponseEntity.status(HttpStatus.LOCKED).body(new ErrorResponse(
				HttpStatus.LOCKED,
				e.getMessage(),
				null));
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException e) {
		log.error("BadCredentialsException: {}", e.getMessage());

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(
				HttpStatus.UNAUTHORIZED,
				e.getMessage(),
				null));
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<?> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
		log.error("DataIntegrityViolationException: {}", e.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(
				HttpStatus.BAD_REQUEST,
				"Something wrong when query database",
				null));
	}

//	@ExceptionHandler(RuntimeException.class)
//	public ResponseEntity<?> handleRuntimeException(RuntimeException e) {
//		log.error("RuntimeException: {}", e.getClass().getCanonicalName());
//		log.error("RuntimeException: {}", e.getMessage());
//
//		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(
//				HttpStatus.UNAUTHORIZED,
//				e.getMessage(),
//				null));
//	}
}
