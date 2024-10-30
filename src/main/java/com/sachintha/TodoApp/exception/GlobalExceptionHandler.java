package com.sachintha.TodoApp.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationExceptions(final MethodArgumentNotValidException e,
			WebRequest request) {
		String errorMessage = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
		ErrorResponse errorResponse = new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
				errorMessage, request.getDescription(false));
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

}
