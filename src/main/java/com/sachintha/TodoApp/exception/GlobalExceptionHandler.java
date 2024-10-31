package com.sachintha.TodoApp.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(final CustomException e, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), e.getStatus().value(), e.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(errorResponse, e.getStatus());
	}

//	this triger when invalide input comming with request
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(final MethodArgumentNotValidException e,
			WebRequest request) {
		String errorMessage = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				errorMessage, request.getDescription(false));
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

//  when user not found in database while authentication
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleGlobalException(UsernameNotFoundException e, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(),
				"Invalid Token, User not found!", request.getDescription(false));
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

//  For any Exception
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGlobalException(Exception e, WebRequest request) {
		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				e.getMessage(), request.getDescription(false));
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}
}
