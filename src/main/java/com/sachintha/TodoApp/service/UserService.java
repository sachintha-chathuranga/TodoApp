package com.sachintha.TodoApp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sachintha.TodoApp.dto.UserRegistrationDto;
import com.sachintha.TodoApp.exception.CustomException;
import com.sachintha.TodoApp.model.User;
import com.sachintha.TodoApp.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
	private final Logger logger = LoggerFactory.getLogger(TaskService.class);
	private final UserRepository userRepository;

	public void createUser(UserRegistrationDto credentials) {
		logger.info("Start creating user");
		try {
			if (userRepository.existsByEmail(credentials.getEmail())) {
				throw new CustomException("Email already in use!", HttpStatus.CONFLICT);
			}
			User user = new User();
			user.setEmail(credentials.getEmail());
			user.setPassword(credentials.getPassword());
			System.out.println(user);
			userRepository.save(user);
			logger.info("user created successfully");
			return;
		} catch (Exception e) {
			logger.error("error while register user", e);
			throw new CustomException(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
}
