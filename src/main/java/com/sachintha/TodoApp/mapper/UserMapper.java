package com.sachintha.TodoApp.mapper;

import com.sachintha.TodoApp.dto.UserDto;
import com.sachintha.TodoApp.model.User;

public class UserMapper {
	public static User mapToUser(UserDto userDto) {
		if (userDto == null) {
			return null;
		}
		User user = new User(userDto.getId(), userDto.getEmail(), userDto.getPassword());
		return user;
	}

	public static UserDto mapToUserDto(User user) {
		if (user == null) {
			return null;
		}
		UserDto userDto = new UserDto(user.getId(), user.getEmail());
		return userDto;
	}
}
