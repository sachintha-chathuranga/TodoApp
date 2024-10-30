package com.sachintha.TodoApp.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
	private Long id;
	private String description;
	private LocalDateTime dueDate;
	private TaskPriority priority;
	private TaskStatus status;

}
