package com.sachintha.TodoApp.exception;

import java.nio.file.AccessDeniedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

@ControllerAdvice
public class GlobalExceptionHandler {
	private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(CustomException.class)
	public ProblemDetail handleCustomException(final CustomException e, WebRequest request) {
		ProblemDetail errorDetail = null;
		errorDetail = ProblemDetail.forStatusAndDetail(e.getStatus(), e.getMessage());
		errorDetail.setProperty("description", request.getDescription(true));
		return errorDetail;
	}

//	this handle when invalid input coming with request
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidationExceptions(final MethodArgumentNotValidException e, WebRequest request) {
		ProblemDetail errorDetail = null;
		String errorMessage = e.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
		errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage);
		errorDetail.setProperty("description", request.getDescription(true));
		return errorDetail;
	}

//  when user not found in database while authentication
	@ExceptionHandler(UsernameNotFoundException.class)
	public ProblemDetail handleGlobalException(UsernameNotFoundException e, WebRequest request) {
		ProblemDetail errorDetail = null;
		errorDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, "Invalid Token");
		errorDetail.setProperty("description", request.getDescription(true));
		return errorDetail;
	}

//  For any Exception
	@ExceptionHandler(Exception.class)
	public ProblemDetail handleGlobalException(Exception e, WebRequest request) {
		ProblemDetail errorDetail = null;
		logger.error(e.getMessage());
		if (e instanceof BadCredentialsException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(401), e.getMessage());
			errorDetail.setProperty("description", "The username or password is incorrect");

			return errorDetail;
		}

		if (e instanceof AccountStatusException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), e.getMessage());
			errorDetail.setProperty("description", "The account is locked");
		}

		if (e instanceof AccessDeniedException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), e.getMessage());
			errorDetail.setProperty("description", "You are not authorized to access this resource");
		}

		if (e instanceof SignatureException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), e.getMessage());
			errorDetail.setProperty("description", "The JWT signature is invalid");
		}

		if (e instanceof ExpiredJwtException) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(403), e.getMessage());
			errorDetail.setProperty("description", "The JWT token has expired");
		}

		if (errorDetail == null) {
			errorDetail = ProblemDetail.forStatusAndDetail(HttpStatusCode.valueOf(500), e.getMessage());
			errorDetail.setProperty("description", "Unknown internal server error.");
		}
		return errorDetail;
	}
}
