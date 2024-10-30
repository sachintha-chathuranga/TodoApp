package com.sachintha.TodoApp.exception;

import org.springframework.http.HttpStatus;

@SuppressWarnings("serial")
public class CustomException extends RuntimeException {
	private HttpStatus status;

	public CustomException(String message, HttpStatus status) {
		super(message);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return this.status;
	}
}
