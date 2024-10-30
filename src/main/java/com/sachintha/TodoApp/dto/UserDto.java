package com.sachintha.TodoApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	private Long id;
	private String email;
	private String password;

	public UserDto(Long id, String email) {
		this.id = id;
		this.email = email;
	}

}
