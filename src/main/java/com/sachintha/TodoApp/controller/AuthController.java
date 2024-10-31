package com.sachintha.TodoApp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sachintha.TodoApp.dto.UserRegistrationDto;
import com.sachintha.TodoApp.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	@Autowired
	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationDto credentials) {
		return authService.createUser(credentials);
	}

//	@PostMapping("/login")
//	public ResponseEntity<AuthResponse> loginUser(@Valid @RequestBody UserRegistrationDto credentials) {
//		authService.authenticateUser(credentials);
//		return ResponseEntity.status(HttpStatus.CREATED).body();
//	}
}
