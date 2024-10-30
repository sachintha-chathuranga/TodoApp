package com.sachintha.TodoApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sachintha.TodoApp.dto.UserRegistrationDto;
import com.sachintha.TodoApp.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	@Autowired
	private final UserService userService;

	@PostMapping("/register")
	public ResponseEntity<String> register(@Valid @RequestBody UserRegistrationDto credentials) {
		userService.createUser(credentials);
		return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
	}
}
