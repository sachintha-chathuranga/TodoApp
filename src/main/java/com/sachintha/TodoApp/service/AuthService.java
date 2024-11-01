package com.sachintha.TodoApp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sachintha.TodoApp.dto.AuthResponse;
import com.sachintha.TodoApp.dto.UserRegistrationDto;
import com.sachintha.TodoApp.dto.UserRole;
import com.sachintha.TodoApp.exception.CustomException;
import com.sachintha.TodoApp.mapper.UserMapper;
import com.sachintha.TodoApp.model.User;
import com.sachintha.TodoApp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final Logger logger = LoggerFactory.getLogger(AuthService.class);
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final JwtService jwtService;

	public ResponseEntity<String> createUser(UserRegistrationDto credentials) {
		logger.info("Start creating user");
		try {
			if (userRepository.existsByEmail(credentials.getEmail())) {
				throw new CustomException("Email already exist!", HttpStatus.CONFLICT);
			}
			User user = new User();
			user.setEmail(credentials.getEmail());
			user.setPassword(passwordEncoder.encode(credentials.getPassword()));
			user.setRole(UserRole.USER);
			userRepository.save(user);
			logger.info("user created successfully");
			return ResponseEntity.ok("user registered successfully");
		} catch (Exception e) {
			logger.error("error while register user", e);
			throw e;
		}
	}

	public AuthResponse authenticateUser(UserRegistrationDto credentials) {
		logger.info("Start Authentication");
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword()));
			logger.info("authentication success!");
			User user = userRepository.findByEmail(credentials.getEmail())
					.orElseThrow(() -> new UsernameNotFoundException("Invalid Email!"));
			logger.trace("User found: {}", user.getEmail());
			String jwtToken = jwtService.generateToken(UserMapper.mapToUserDto(user));
			logger.info("token genarate successfully");
			return AuthResponse.builder().token(jwtToken).build();
		} catch (Exception e) {
			logger.error("Error occurred in authenticateUser()");
			throw e;
		}
	}
}
