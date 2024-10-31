package com.sachintha.TodoApp.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateDto {
	@NotNull(message = "task id is required!")
	private Long id;

	@Size(min = 5, max = 50, message = "Description must be between 5 and 50 characters!")
	private String description;

	@Future(message = "Due date must be in the future!")
	private LocalDateTime dueDate;

	private TaskPriority priority;

	private TaskStatus status;
}
