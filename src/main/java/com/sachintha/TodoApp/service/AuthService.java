package com.sachintha.TodoApp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sachintha.TodoApp.dto.UserRegistrationDto;
import com.sachintha.TodoApp.exception.CustomException;
import com.sachintha.TodoApp.model.User;
import com.sachintha.TodoApp.model.UserRole;
import com.sachintha.TodoApp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final Logger logger = LoggerFactory.getLogger(TaskService.class);
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public ResponseEntity<String> createUser(UserRegistrationDto credentials) {
		logger.info("Start creating user");
		try {
			if (userRepository.existsByEmail(credentials.getEmail())) {
				throw new CustomException("Email already in use!", HttpStatus.CONFLICT);
			}
			User user = new User();
			user.setEmail(credentials.getEmail());
			user.setPassword(passwordEncoder.encode(credentials.getPassword()));
			logger.trace("Encoded password: {}", user.getPassword());
			user.setRole(UserRole.USER);
			userRepository.save(user);
			logger.info("user created successfully");
			return ResponseEntity.ok("user registered successfully");
		} catch (Exception e) {
			logger.error("error while register user", e);
			throw e;
		}
	}

	public void authenticateUser(UserRegistrationDto credentials) {
		// TODO Auto-generated method stub

	}
}
