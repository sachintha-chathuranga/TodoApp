package com.sachintha.TodoApp.service;

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
	private final UserRepository userRepository;

	public void createUser(UserRegistrationDto credentials) {
		if (userRepository.existsByEmail(credentials.getEmail())) {
			throw new CustomException("Email already in use!", HttpStatus.CONFLICT);
		}
		User user = new User();
		user.setEmail(credentials.getEmail());
		user.setPassword(credentials.getPassword());
		userRepository.save(user);
		return;
	}
}
