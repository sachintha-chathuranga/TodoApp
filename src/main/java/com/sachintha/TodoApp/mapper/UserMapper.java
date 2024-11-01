package com.sachintha.TodoApp.mapper;

import com.sachintha.TodoApp.dto.UserDto;
import com.sachintha.TodoApp.model.User;

public class UserMapper {
	public static UserDto mapToUserDto(User user) {
		if (user == null) {
			return null;
		}
		UserDto userDto = new UserDto(user.getId(), user.getEmail(), user.getRole());
		return userDto;
	}
}
